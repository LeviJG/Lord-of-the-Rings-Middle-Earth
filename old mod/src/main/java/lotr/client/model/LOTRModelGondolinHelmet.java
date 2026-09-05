package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelGondolinHelmet extends LOTRModelBiped {
	public LOTRModelGondolinHelmet() {
		this(0.0f);
	}

	public LOTRModelGondolinHelmet(float f) {
		super(f);
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f);
		bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f);
		bipedHead.setTextureOffset(46, 0).addBox(-0.5f, -14.0f - f, -4.5f, 1, 6, 1, 0.0f);
		bipedHead.setTextureOffset(50, 0).addBox(-0.5f, -12.0f - f, -0.5f, 1, 4, 1, 0.0f);
		bipedHead.setTextureOffset(54, 0).addBox(-0.5f, -10.0f - f, 3.5f, 1, 2, 1, 0.0f);
		bipedHead.setTextureOffset(32, -7).addBox(0.0f, -13.5f - f, -3.5f, 0, 6, 7, 0.0f);
		bipedHeadwear.cubeList.clear();
		bipedBody.cubeList.clear();
		bipedRightArm.cubeList.clear();
		bipedLeftArm.cubeList.clear();
		bipedRightLeg.cubeList.clear();
		bipedLeftLeg.cubeList.clear();
	}
}
