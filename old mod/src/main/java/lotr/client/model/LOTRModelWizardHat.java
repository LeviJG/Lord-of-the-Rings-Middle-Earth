package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelWizardHat extends LOTRModelBiped {
	public ModelRenderer hatBrim;
	public ModelRenderer hat1;
	public ModelRenderer hat2;
	public ModelRenderer hat3;

	public LOTRModelWizardHat() {
		this(0.0f);
	}

	public LOTRModelWizardHat(float f) {
		super(f);
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f);
		hatBrim = new ModelRenderer(this, 0, 17);
		hatBrim.setRotationPoint(0.0f, 0.0f, 0.0f);
		hatBrim.addBox(-7.0f, -8.0f - f, -7.0f, 14, 1, 14);
		hat1 = new ModelRenderer(this, 32, 3);
		hat1.setRotationPoint(0.0f, -8.0f - f, 0.0f);
		hat1.addBox(-4.0f, -5.0f, -4.0f, 8, 5, 8);
		hat2 = new ModelRenderer(this, 11, 7);
		hat2.setRotationPoint(0.0f, -4.0f, 0.0f);
		hat2.addBox(-2.5f, -4.0f, -2.5f, 5, 4, 5);
		hat3 = new ModelRenderer(this, 0, 22);
		hat3.setRotationPoint(0.0f, -3.5f, 0.0f);
		hat3.addBox(-1.5f, -3.0f, -1.0f, 3, 3, 3);
		bipedHead.addChild(hatBrim);
		hatBrim.addChild(hat1);
		hat1.addChild(hat2);
		hat2.addChild(hat3);
		bipedHeadwear.cubeList.clear();
		bipedBody.cubeList.clear();
		bipedRightArm.cubeList.clear();
		bipedLeftArm.cubeList.clear();
		bipedRightLeg.cubeList.clear();
		bipedLeftLeg.cubeList.clear();
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bipedHead.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		hat2.rotateAngleX = (float) Math.toRadians(-(10.0 + f1 * 10.0));
		hat3.rotateAngleX = (float) Math.toRadians(-(10.0 + f1 * 10.0));
	}
}
