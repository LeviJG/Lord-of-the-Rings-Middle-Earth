"""Rebuilds the mod's chest sheets from the 1.7.10 originals for MC 26.2.

    python3 tools/fix_chest_textures.py        (run from the repo root)

Idempotent: it always reads from `old mod/` and writes to `src/`, so re-running
it or changing a flag below re-derives the sheets rather than compounding.

WHY ANY OF THIS IS NEEDED
-------------------------
The two versions disagree about which way up the chest model is.

  1.7.10  LOTRRenderChest:  translate(x, y + 1, z + 1); scale(1, -1, -1)
  26.2    ChestRenderer:    Matrix4f.rotationAround(YP(-toYRot()), 0.5, 0, 0.5)

The modern transform is a pure rotation about Y -- no flip at all -- while the
old one negates BOTH y and z. The cube UV layout itself is unchanged between the
versions, so texture-cell to model-space is the same map in both; only
model-space to world differs, and it differs by exactly a 180 degree rotation
about the X axis.

Rotating a cube 180 degrees about X sends minY to maxY and minZ to maxZ, and
leaves the two X faces where they are, so:

    DOWN  <-> UP      swap the cells, and flip each (the z axis is negated)
    NORTH <-> SOUTH   swap the cells, and flip each (the y axis is negated)
    WEST, EAST        stay put, but rotate 180 (both in-plane axes negated)

CONFIRMED, NOT DERIVED
----------------------
Two checks against art rather than recollection:

  * In the 1.7.10 sheets the lid's last cell row is byte-identical to the body's
    first cell row -- they are the seam where the two parts meet. The lid spans
    world y 14..9 and the body y 10..0, so cell v runs DOWNWARD in world.
  * In vanilla's modern normal.png the latch detail sits at lid row 1 and body
    rows 7-8. Those are the same physical place, the lid/body junction, which
    only holds if cell v runs UPWARD in world.

Hence the vertical flips above. Vanilla's latch face is also in the SOUTH cell
while the LOTR art has its decorated face in NORTH, which is the z swap.

All three parts get the same treatment, which is what the geometry demands. An
earlier pass fixed only the DOWN/UP cells and left the four side cells alone;
with the sides still in the old convention the body looked correct only because
two errors cancelled, so reverting its DOWN/UP swap seemed right at the time.
Once the sides were fixed the body's top and bottom were plainly transposed
again, so it swaps like everything else.
"""
from PIL import Image

SRC = 'old mod/src/main/resources/assets/lotr/item/chest/'
DST = 'src/main/resources/assets/lotr/textures/entity/chest/'

CHESTS = [("lebethron", "lebethron"), ("basket", "basket"),
          ("mallorn", "mallorn"), ("ancientHarad", "ancient_harad"),
          ("stone", "stone")]

# name -> (texOffsU, texOffsV, w, h, d, swap_horizontal_cells)
PARTS = [("lid",    0,  0, 14,  5, 14, True),
         ("bottom", 0, 19, 14, 10, 14, True),
         ("lock",   0,  0,  2,  4,  1, True)]

VFLIP = Image.FLIP_TOP_BOTTOM
HFLIP = Image.FLIP_LEFT_RIGHT


def swap(im, a, b, transform):
    """Exchange two equally sized cells, putting each through `transform`."""
    pa, pb = im.crop(a), im.crop(b)
    im.paste(transform(pb), a)
    im.paste(transform(pa), b)


def rotate180_in_place(im, box):
    im.paste(im.crop(box).transpose(VFLIP).transpose(HFLIP), box)


def fix_part(im, u, v, w, h, d, swap_horizontal):
    down  = (u + d,         v,     u + d + w,      v + d)
    up    = (u + d + w,     v,     u + d + 2 * w,  v + d)
    west  = (u,             v + d, u + d,          v + d + h)
    north = (u + d,         v + d, u + d + w,      v + d + h)
    east  = (u + d + w,     v + d, u + 2 * d + w,  v + d + h)
    south = (u + 2 * d + w, v + d, u + 2 * d + 2 * w, v + d + h)

    if swap_horizontal:
        swap(im, down, up, lambda c: c.transpose(VFLIP))
    swap(im, north, south, lambda c: c.transpose(VFLIP))
    rotate180_in_place(im, west)
    rotate180_in_place(im, east)


for src, dst in CHESTS:
    im = Image.open(SRC + src + '.png').convert('RGBA')
    assert im.size == (64, 64), (src, im.size)
    for name, u, v, w, h, d, swap_horizontal in PARTS:
        fix_part(im, u, v, w, h, d, swap_horizontal)
    im.save(DST + dst + '.png')
    print(f"{src}.png -> {dst}.png")
