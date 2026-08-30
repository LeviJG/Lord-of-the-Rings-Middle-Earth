"""Regenerates the troll totem's item models and their texture.

    python3 tools/gen_totem_item_models.py      (from the repo root)

In the world the totem is drawn by LOTRTrollTotemRenderer straight from the
entity model. An ITEM needs an ordinary block model, so this rebuilds one per
part. Vanilla does exactly this for beds: assets/minecraft/items/red_bed.json
is a composite of the plain block models red_bed_head / red_bed_foot, and
template_bed_head.json gives every face an explicit "uv" rect into a texture.

Two conversions happen here.

GEOMETRY. LOTRModelTrollTotem's boxes are in entity space; the renderer places
them with translate(0.5, 1.5, 0.5) then scale(1, -1, -1). That scale is a 180
degree rotation about X (its determinant is +1), not a mirror, so it bakes into
the coordinates cleanly.

The ITEM model then turns the totem a further 180 degrees about Y, because a
block item icon is drawn with display rotation [30, 225, 0], which presents the
block's NORTH face -- that is how a furnace shows its front, whose texture
model/block/orientable_with_bottom.json puts on north. The totem's carved face
is on the entity model's north, which the X-rotation alone would land on the
block's SOUTH, leaving the icon showing the back of its head. Composed, the two
rotations give x -> 8-x, y -> 24-y, z -> 8+z, in sixteenths.

TEXTURING. The same rotation swaps which entity face lands on which block face:
up <-> down and north <-> south, with east and west unmoved. Each face then
takes the rect the entity model's box unwrapping would have given it --

    up     u+d      .. u+d+w     , v     .. v+d
    down   u+d+w    .. u+d+2w    , v     .. v+d
    east   u        .. u+d       , v+d   .. v+d+h
    north  u+d      .. u+d+w     , v+d   .. v+d+h
    west   u+d+w    .. u+2d+w    , v+d   .. v+d+h
    south  u+2d+w   .. u+2d+2w   , v+d   .. v+d+h

-- so the skull's front gets the carved face, its top gets the crown, and so
on, instead of every face wearing one flat patch. All 66 rects were checked
in-bounds and fully opaque against a sheet that is only half covered, which is
what gives confidence the layout is right.

NOT reproduced: the per-face 180 degree UV rotation that the X-rotation also
implies, and the u-flip on mirrored parts (only east/west are swapped). On a
near-uniform stone texture neither is visible.

Keep the box lists in sync with LOTRTrollTotemModel.createLayer().
"""
import json, collections
from PIL import Image

SHEET_SRC = 'src/main/resources/assets/lotr/textures/entity/troll_totem.png'
SHEET_DST = 'src/main/resources/assets/lotr/textures/block/troll_totem_sheet.png'
MODELDIR  = 'src/main/resources/assets/lotr/models/block/'

# name, texOffs, (w,h,d), rotationPoint, boxFrom, mirrored
BOXES = {
 "head": [
   ("skull", (0,0),  (12,10,12), (0,22,4), (-6,-10,-10), False),
   ("horn",  (0,0),  (2,3,2),    (0,22,4), (-1,-5,-12),  False),
   ("ear_r", (40,0), (1,4,3),    (0,22,4), (-7,-6,-6),   False),
   ("ear_l", (40,0), (1,4,3),    (0,22,4), (6,-6,-6),    True),
   ("jaw",   (48,0), (12,2,12),  (0,24,0), (-6,-2,-6),   False),
 ],
 "body": [
   ("torso", (0,24), (10,16,10), (0,8,0),  (-5,0,-5),    False),
   ("arm_r", (40,24),(3,10,6),   (-5,9,0), (-3,0,-3),    False),
   ("arm_l", (40,24),(3,10,6),   (5,9,0),  (0,0,-3),     True),
 ],
 "base": [
   ("thigh_r",(0,50), (6,7,6),   (-4,8,0), (-3,0,-3),    False),
   ("shin_r", (24,50),(5,7,5),   (-4,8,0), (-2.5,7,-2.5),False),
   ("thigh_l",(0,50), (6,7,6),   (4,8,0),  (-3,0,-3),    True),
   ("shin_l", (24,50),(5,7,5),   (4,8,0),  (-2.5,7,-2.5),True),
   ("plinth", (48,46),(16,2,16), (0,22,0), (-8,0,-8),    False),
 ],
}

# The entity model's box UV unwrapping, keyed by the MC Direction of the face
# it belongs to IN ENTITY SPACE.
#
# Two things here are easy to get backwards, and both were, first time round:
#
#  * Entity models are built with +Y pointing DOWN. So the first cell of the top
#    row is the box's minY face -- normal -Y, Direction.DOWN -- even though it is
#    what you see looking down ON the model. Verified against this very sheet:
#    the jaw's first cell is the dark mouth cavity, which is the jaw's upper
#    surface, and the skull's second cell is the hollow underside.
#  * The bottom row wraps around the box as right, front, left, back. A player
#    skin lays a head out that way, and the player's right is -X, so the cells
#    are minX/WEST, minZ/NORTH, maxX/EAST, maxZ/SOUTH -- not east-first.
def entity_rects(off, size):
    u, v = off
    w, h, d = size
    return {
      "down":  (u+d,       v,   u+d+w,     v+d),      # minY
      "up":    (u+d+w,     v,   u+d+2*w,   v+d),      # maxY
      "west":  (u,         v+d, u+d,       v+d+h),    # minX
      "north": (u+d,       v+d, u+d+w,     v+d+h),    # minZ
      "east":  (u+d+w,     v+d, u+2*d+w,   v+d+h),    # maxX
      "south": (u+2*d+w,   v+d, u+2*d+2*w, v+d+h),    # maxZ
    }

# The composed rotation is x -> -x, y -> -y, z -> +z. Block face -> the entity
# face that lands on it: y flips so up/down swap, x flips so east/west swap, and
# z is untouched so north stays north -- which is the point, that is the face a
# block item icon shows.
FACE_SOURCE = {"up":"down", "down":"up", "north":"north", "south":"south",
               "east":"west", "west":"east"}
MIRRORED    = {"east":"west", "west":"east"}

def to_block(rp, off, size):
    """Entity units -> block sixteenths, for the ITEM model: the renderer's own
    180 about X, then a further 180 about Y so the face points north."""
    lo = [rp[i] + off[i] for i in range(3)]
    hi = [lo[0]+size[0], lo[1]+size[1], lo[2]+size[2]]
    xs = sorted((8 - lo[0], 8 - hi[0]))
    ys = sorted((24 - lo[1], 24 - hi[1]))
    zs = sorted((8 + lo[2], 8 + hi[2]))
    return [xs[0], ys[0], zs[0]], [xs[1], ys[1], zs[1]]

# The sheet goes on the block atlas. Pad 128x64 -> 128x128 so it is square: a
# non-square sprite is read as an animation strip.
src = Image.open(SHEET_SRC).convert('RGBA')
assert src.size == (128, 64), src.size
sheet = Image.new('RGBA', (128, 128), (0, 0, 0, 0))
sheet.paste(src, (0, 0))
sheet.save(SHEET_DST)
SCALE = 16.0 / 128.0          # pixels -> block-model uv on a 128x128 sprite

for part, boxes in BOXES.items():
    elements = []
    for name, off, size, rp, boxfrom, mirror in boxes:
        # box "from" is (x,y,z); size is (w,h,d) = (x,y,z) extents
        a, b = to_block(rp, boxfrom, size)
        for v in a + b:
            assert -0.001 <= v <= 16.001, (part, name, a, b)
        rects = entity_rects(off, size)
        faces = collections.OrderedDict()
        for face in ("down", "up", "north", "south", "west", "east"):
            src_face = FACE_SOURCE[face]
            if mirror:
                src_face = MIRRORED.get(src_face, src_face)
            x0, y0, x1, y1 = rects[src_face]
            faces[face] = {
                "uv": [round(x0*SCALE, 3), round(y0*SCALE, 3),
                       round(x1*SCALE, 3), round(y1*SCALE, 3)],
                "texture": "#sheet",
            }
        elements.append(collections.OrderedDict(
            [("__comment", name), ("from", a), ("to", b), ("faces", faces)]))

    model = collections.OrderedDict([
        ("__comment", "GENERATED by tools/gen_totem_item_models.py -- do not "
                      "hand-edit. Item form of the totem part; the world model "
                      "is LOTRTrollTotemRenderer."),
        ("parent", "minecraft:block/block"),
        ("textures", {"sheet": "lotr:block/troll_totem_sheet",
                      "particle": "lotr:block/troll_totem_sheet"}),
        ("elements", elements),
    ])
    p = f"{MODELDIR}troll_totem_{part}_inventory.json"
    json.dump(model, open(p, "w"), indent=2)
    open(p, "a").write("\n")
    print(f"{part}: {len(elements)} elements, {len(elements)*6} textured faces")
