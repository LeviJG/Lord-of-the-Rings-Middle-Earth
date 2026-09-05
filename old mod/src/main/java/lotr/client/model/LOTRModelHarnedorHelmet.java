package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelHarnedorHelmet extends LOTRModelBiped {
	public LOTRModelHarnedorHelmet() {
		this(0.0f);
	}

	public LOTRModelHarnedorHelmet(float f) {
		super(f);
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f);
		bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f);
		bipedHead.setTextureOffset(0, 5).addBox(0.0f, -11.0f, -7.0f, 0, 10, 14, 0.0f);
		bipedHead.setTextureOffset(16, 19).addBox(-6.0f, -2.0f, -6.0f, 12, 0, 12, 0.0f);
		bipedHeadwear.cubeList.clear();
		bipedBody.cubeList.clear();
		bipedRightArm.cubeList.clear();
		bipedLeftArm.cubeList.clear();
		bipedRightLeg.cubeList.clear();
		bipedLeftLeg.cubeList.clear();
	}
}
