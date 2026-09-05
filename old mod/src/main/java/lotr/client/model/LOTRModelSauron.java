package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelSauron extends LOTRModelBiped {
	public ModelRenderer bipedCape;

	public LOTRModelSauron() {
		bipedHead = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8);
		bipedHead.setRotationPoint(0.0f, -12.0f, 0.0f);
		bipedHead.setTextureOffset(32, 0).addBox(-0.5f, -15.0f, -3.5f, 1, 7, 1);
		bipedHead.setTextureOffset(32, 0).addBox(-2.5f, -13.0f, -3.5f, 1, 5, 1);
		bipedHead.setTextureOffset(32, 0).addBox(1.5f, -13.0f, -3.5f, 1, 5, 1);
		bipedHead.setTextureOffset(32, 0).addBox(-0.5f, -16.0f, 2.5f, 1, 8, 1);
		bipedHead.setTextureOffset(32, 0).addBox(-3.5f, -16.0f, -0.5f, 1, 8, 1);
		bipedHead.setTextureOffset(32, 0).addBox(2.5f, -16.0f, -0.5f, 1, 8, 1);
		bipedBody = new ModelRenderer(this, 40, 42).setTextureSize(64, 64);
		bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 18, 4);
		bipedBody.setRotationPoint(0.0f, -12.0f, 0.0f);
		bipedRightArm = new ModelRenderer(this, 0, 43).setTextureSize(64, 64);
		bipedRightArm.addBox(-3.0f, -2.0f, -2.0f, 4, 17, 4);
		bipedRightArm.setRotationPoint(-5.0f, -8.0f, 0.0f);
		bipedRightArm.setTextureOffset(16, 52).addBox(-4.0f, -3.0f, -3.0f, 6, 6, 6);
		bipedLeftArm = new ModelRenderer(this, 0, 43).setTextureSize(64, 64);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 17, 4);
		bipedLeftArm.setRotationPoint(5.0f, -8.0f, 0.0f);
		bipedLeftArm.setTextureOffset(16, 52).addBox(-2.0f, -3.0f, -3.0f, 6, 6, 6);
		bipedRightLeg = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
		bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 18, 4);
		bipedRightLeg.setRotationPoint(-2.0f, 6.0f, 0.0f);
		bipedLeftLeg = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 18, 4);
		bipedLeftLeg.setRotationPoint(2.0f, 6.0f, 0.0f);
		bipedCape = new ModelRenderer(this, 38, 0).setTextureSize(64, 64);
		bipedCape.addBox(-6.0f, 1.0f, 1.0f, 12, 32, 1);
		bipedCape.setRotationPoint(0.0f, -12.0f, 0.0f);
		bipedCape.rotateAngleX = 0.15f;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bipedHead.render(f5);
		bipedBody.render(f5);
		bipedRightArm.render(f5);
		bipedLeftArm.render(f5);
		bipedRightLeg.render(f5);
		bipedLeftLeg.render(f5);
		bipedCape.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (isSneak) {
			bipedRightLeg.rotationPointY = 3.0f;
			bipedLeftLeg.rotationPointY = 3.0f;
			bipedHead.rotationPointY = -11.0f;
		} else {
			bipedRightLeg.rotationPointY = 6.0f;
			bipedLeftLeg.rotationPointY = 6.0f;
			bipedHead.rotationPointY = -12.0f;
		}
	}
}
