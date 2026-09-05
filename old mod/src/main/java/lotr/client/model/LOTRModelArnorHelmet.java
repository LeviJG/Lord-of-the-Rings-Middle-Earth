package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelArnorHelmet extends LOTRModelBiped {
	public LOTRModelArnorHelmet() {
		this(0.0f);
	}

	public LOTRModelArnorHelmet(float f) {
		super(f);
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f);
		bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f);
		bipedHead.setTextureOffset(32, 0).addBox(-4.5f - f, -13.0f - f, -1.0f, 1, 8, 1, 0.0f);
		bipedHead.setTextureOffset(36, 0).addBox(-4.5f - f, -12.0f - f, 0.0f, 1, 7, 1, 0.0f);
		bipedHead.setTextureOffset(40, 0).addBox(-4.5f - f, -11.0f - f, 1.0f, 1, 5, 1, 0.0f);
		bipedHead.mirror = true;
		bipedHead.setTextureOffset(32, 0).addBox(3.5f + f, -13.0f - f, -1.0f, 1, 8, 1, 0.0f);
		bipedHead.setTextureOffset(36, 0).addBox(3.5f + f, -12.0f - f, 0.0f, 1, 7, 1, 0.0f);
		bipedHead.setTextureOffset(40, 0).addBox(3.5f + f, -11.0f - f, 1.0f, 1, 5, 1, 0.0f);
		bipedHeadwear.cubeList.clear();
		bipedBody.cubeList.clear();
		bipedRightArm.cubeList.clear();
		bipedLeftArm.cubeList.clear();
		bipedRightLeg.cubeList.clear();
		bipedLeftLeg.cubeList.clear();
	}
}
