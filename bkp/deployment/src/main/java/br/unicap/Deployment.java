package br.unicap;

class Common {

    private static final String FEATURE = "greeting";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

}
