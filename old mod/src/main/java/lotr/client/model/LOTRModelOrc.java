package lotr.client.model;

import lotr.client.render.entity.LOTRGlowingEyes;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelOrc extends LOTRModelBiped implements LOTRGlowingEyes.Model {
	public ModelRenderer nose = new ModelRenderer(this, 14, 17);
	public ModelRenderer earRight;
	public ModelRenderer earLeft;

	public LOTRModelOrc() {
		this(0.0f);
	}

	public LOTRModelOrc(float f) {
		super(f);
		nose.addBox(-0.5f, -4.0f, -4.8f, 1, 2, 1, f);
		nose.setRotationPoint(0.0f, 0.0f, 0.0f);
		earRight = new ModelRenderer(this, 0, 0);
		earRight.addBox(-3.5f, -5.5f, 2.0f, 1, 2, 3, f);
		earRight.setRotationPoint(0.0f, 0.0f, 0.0f);
		earRight.rotateAngleX = 0.2617994f;
		earRight.rotateAngleY = -0.5235988f;
		earRight.rotateAngleZ = -0.22689281f;
		earLeft = new ModelRenderer(this, 24, 0);
		earLeft.addBox(2.5f, -5.5f, 2.0f, 1, 2, 3, f);
		earLeft.setRotationPoint(0.0f, 0.0f, 0.0f);
		earLeft.rotateAngleX = 0.2617994f;
		earLeft.rotateAngleY = 0.5235988f;
		earLeft.rotateAngleZ = 0.22689281f;
		bipedHead.addChild(nose);
		bipedHead.addChild(earRight);
		bipedHead.addChild(earLeft);
	}

	@Override
	public void renderGlowingEyes(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bipedHead.render(f5);
	}
}
