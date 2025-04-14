plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.5-fabric" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledPublish", stonecutter.chiseled) {
    group = "project"
    ofTask("publish")
}

stonecutter registerChiseled tasks.register("chiseledPublishToMavenLocal", stonecutter.chiseled) {
    group = "project"
    ofTask("publishToMavenLocal")
}
