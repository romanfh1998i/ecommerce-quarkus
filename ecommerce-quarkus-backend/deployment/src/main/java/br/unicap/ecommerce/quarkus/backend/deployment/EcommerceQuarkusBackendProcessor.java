package br.unicap.ecommerce.quarkus.backend.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class EcommerceQuarkusBackendProcessor {

    private static final String FEATURE = "ecommerce-quarkus-backend";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

}
