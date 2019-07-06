package chpg.visualizations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Resources {

	public static void main(String[] args) throws IOException {
		File file = new File("./templates");
		Files.walk(Paths.get(file.toPath().toUri()))
        .filter(Files::isRegularFile)
        .forEach(System.out::println);
	}
	
	public static List<String> getResources() {
		List<String> resources = new ArrayList<String>();
		resources.add("templates/index.html");
		resources.add("templates/font-awesome-4.0.3/css/font-awesome.css");
		resources.add("templates/font-awesome-4.0.3/css/font-awesome.min.css");
		resources.add("templates/font-awesome-4.0.3/less/list.less");
		resources.add("templates/font-awesome-4.0.3/less/stacked.less");
		resources.add("templates/font-awesome-4.0.3/less/core.less");
		resources.add("templates/font-awesome-4.0.3/less/fixed-width.less");
		resources.add("templates/font-awesome-4.0.3/less/variables.less");
		resources.add("templates/font-awesome-4.0.3/less/rotated-flipped.less");
		resources.add("templates/font-awesome-4.0.3/less/font-awesome.less");
		resources.add("templates/font-awesome-4.0.3/less/icons.less");
		resources.add("templates/font-awesome-4.0.3/less/spinning.less");
		resources.add("templates/font-awesome-4.0.3/less/path.less");
		resources.add("templates/font-awesome-4.0.3/less/bordered-pulled.less");
		resources.add("templates/font-awesome-4.0.3/less/larger.less");
		resources.add("templates/font-awesome-4.0.3/less/mixins.less");
		resources.add("templates/font-awesome-4.0.3/scss/_stacked.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_variables.scss");
		resources.add("templates/font-awesome-4.0.3/scss/font-awesome.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_rotated-flipped.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_path.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_list.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_larger.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_spinning.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_core.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_mixins.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_icons.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_fixed-width.scss");
		resources.add("templates/font-awesome-4.0.3/scss/_bordered-pulled.scss");
		resources.add("templates/font-awesome-4.0.3/fonts/fontawesome-webfont.svg");
		resources.add("templates/font-awesome-4.0.3/fonts/FontAwesome.otf");
		resources.add("templates/font-awesome-4.0.3/fonts/fontawesome-webfont.ttf");
		resources.add("templates/font-awesome-4.0.3/fonts/fontawesome-webfont.woff");
		resources.add("templates/font-awesome-4.0.3/fonts/fontawesome-webfont.eot");
		resources.add("templates/css/cytoscape.js-panzoom.css");
		resources.add("templates/css/cytoscape-context-menus.css");
		resources.add("templates/css/cytoscape.js-navigator.css");
		resources.add("templates/js/jquery-2.0.3.min.js");
		resources.add("templates/js/cytoscape-dagre.js");
		resources.add("templates/js/dagre.min.js");
		resources.add("templates/js/cytoscape.min.js");
		resources.add("templates/js/cytoscape-navigator.js");
		resources.add("templates/js/cytoscape-panzoom.js");
		resources.add("templates/js/cytoscape-context-menus.js");
		resources.add("templates/js/cytoscape-cxtmenu.js");
		resources.add("templates/js/klay.min.js");
		resources.add("templates/js/cytoscape-klay.js");
		resources.add("templates/icons/add.svg");
		resources.add("templates/icons/remove.svg");
		return resources;
	}
	
}
