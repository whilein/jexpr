/*
 *    Copyright 2022 Whilein
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.whilein.jexpr.tools;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Graph {

    private static final DocumentFactory DF = DocumentFactory.getInstance();

    Document document;

    Element graph;

    @NonFinal
    int idCounter;

    public static Graph create() {
        val document = DF.createDocument();

        val root = createSection("xgml");

        addAttribute(root, "Creator", "jexpr-tools", "String");
        addAttribute(root, "Version", "2.18", "String");

        document.add(root);

        val graph = createSection("graph");
        addAttribute(graph, "hierarchic", 1, "int");
        addAttribute(graph, "directed", 1, "int");
        addAttribute(graph, "label", "", "String");

        root.add(graph);

        return new Graph(document, graph);
    }

    public int addNode(final double x, final double y, final double w, final double h,
                       final String label, final int color) {
        val id = idCounter++;

        val node = createSection("node");
        addAttribute(node, "id", id, "int");
        addAttribute(node, "label", label, "String");

        val graphics = createSection("graphics");
        addAttribute(graphics, "x", x, "double");
        addAttribute(graphics, "y", y, "double");
        addAttribute(graphics, "w", w, "double");
        addAttribute(graphics, "h", h, "double");
        addAttribute(graphics, "fill", color(color), "String");
        addAttribute(graphics, "outline", color(0), "String");
        addAttribute(graphics, "raisedBorder", false, "boolean");
        addAttribute(graphics, "type", "rectangle", "String");

        node.add(graphics);

        graph.add(node);

        return id;
    }

    public void addEdge(final int source, final int target) {
        val edge = createSection("edge");
        addAttribute(edge, "source", source, "int");
        addAttribute(edge, "target", target, "int");

        val graphics = createSection("graphics");
        addAttribute(graphics, "fill", color(0), "String");
        addAttribute(graphics, "targetArrow", "standard", "String");

        edge.add(graphics);

        graph.add(edge);
    }

    private static Element createSection(final String name) {
        val root = DF.createElement("section");
        root.setAttributes(Collections.singletonList(DF.createAttribute(root, "name", name)));

        return root;
    }

    private static String color(final int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    private static void addAttribute(
            final Element element,
            final String key,
            final Object value,
            final String type
    ) {
        val attribute = element.addElement("attribute");
        attribute.setAttributes(Arrays.asList(
                DF.createAttribute(attribute, "key", key),
                DF.createAttribute(attribute, "type", type)
        ));
        attribute.setText(String.valueOf(value));
    }

    public void save(final Path location) throws IOException {
        val format = OutputFormat.createPrettyPrint();
        format.setIndentSize(4);
        format.setEncoding("UTF-8");

        try (val bw = Files.newBufferedWriter(location)) {
            new XMLWriter(bw, format).write(document);
        }
    }

}
