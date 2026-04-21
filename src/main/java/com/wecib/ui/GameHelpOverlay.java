package com.wecib.ui;

import com.wecib.model.CardType;
import com.wecib.util.TypeChart;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Full-screen help: energy rules, battle basics, and type matchup reference.
 */
public final class GameHelpOverlay {

    /** Short text for hover on the ENERGY label in battle. */
    public static final String ENERGY_TOOLTIP =
            "You start at 1 energy. At the start of each of your turns, gain +1 (maximum 5). "
                    + "Each attack costs energy (the small dots). Bench swaps are free.";

    private GameHelpOverlay() {}

    public static void show(Node anchor) {
        show(anchor, null);
    }

    /**
     * @param onClose optional callback when overlay is dismissed (e.g. mark first-run seen)
     */
    public static void show(Node anchor, Runnable onClose) {
        StackPane root = findStackPaneRoot(anchor);
        if (root == null) return;

        Rectangle dim = new Rectangle();
        dim.widthProperty().bind(root.widthProperty());
        dim.heightProperty().bind(root.heightProperty());
        dim.setFill(Color.rgb(0, 0, 0, 0.72));
        dim.setMouseTransparent(false);

        VBox panel = new VBox(14);
        panel.setMaxWidth(720);
        panel.setPadding(new Insets(28, 32, 28, 32));
        panel.setAlignment(Pos.TOP_CENTER);
        panel.getStyleClass().add("help-panel");

        Label title = new Label("How to play");
        title.getStyleClass().add("help-title");

        Label intro = new Label(
                "Draft a deck, then battle. Knock out all rival cards to win.\n"
                        + "Hover type badges and attacks on cards for matchup hints."
        );
        intro.getStyleClass().add("help-intro");
        intro.setWrapText(true);

        Label energyHeader = sectionHeader("Energy");
        Label energyBody = bodyLabel(
                "You start each battle with 1 energy. At the start of your turn, you gain 1 more (up to 5).\n"
                        + "Each attack shows small dots — that is its energy cost. Grayed-out attacks are too expensive until you save energy.\n"
                        + "Bench swaps do not cost energy (click a bench card on your turn)."
        );

        Label typesHeader = sectionHeader("Types & damage");
        Label typesBody = bodyLabel(
                "Each attack has a type. When it hits the defending card, damage is multiplied:\n"
                        + "• 2× — super effective\n"
                        + "• 1× — normal\n"
                        + "• 0.5× — not very effective\n"
                        + "Hover attack buttons in battle to preview damage against the current opponent."
        );

        Label chartHeader = sectionHeader("Type chart (attack → defender)");
        VBox chartBox = new VBox(6);
        chartBox.getStyleClass().add("help-chart-box");
        for (CardType atk : CardType.values()) {
            Label row = new Label(formatChartRow(atk));
            row.getStyleClass().add("help-chart-row");
            row.setWrapText(true);
            chartBox.getChildren().add(row);
        }

        ScrollPane scroll = new ScrollPane(chartBox);
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(280);
        scroll.setMinHeight(200);
        scroll.getStyleClass().add("help-scroll");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        spacer.setMinHeight(8);

        Button close = new Button("Got it");
        close.getStyleClass().add("help-close-button");

        panel.getChildren().addAll(
                title, intro,
                energyHeader, energyBody,
                typesHeader, typesBody,
                chartHeader, scroll,
                spacer,
                close
        );

        StackPane overlay = new StackPane(dim, panel);
        StackPane.setAlignment(panel, Pos.CENTER);
        overlay.setPickOnBounds(true);

        close.setOnAction(e -> {
            root.getChildren().remove(overlay);
            if (onClose != null) onClose.run();
        });

        root.getChildren().add(overlay);
    }

    private static String formatChartRow(CardType atk) {
        StringBuilder strong = new StringBuilder();
        StringBuilder weak = new StringBuilder();
        for (CardType def : CardType.values()) {
            double m = TypeChart.getMultiplier(atk, def);
            if (m > 1.0) {
                if (strong.length() > 0) strong.append(", ");
                strong.append(def.displayName());
            } else if (m < 1.0) {
                if (weak.length() > 0) weak.append(", ");
                weak.append(def.displayName());
            }
        }
        return atk.displayName() + ": 2× vs " + strong + "  |  0.5× vs " + weak;
    }

    private static Label sectionHeader(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("help-section-header");
        return l;
    }

    private static Label bodyLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("help-body");
        l.setWrapText(true);
        return l;
    }

    private static StackPane findStackPaneRoot(Node node) {
        if (node == null || node.getScene() == null) return null;
        javafx.scene.Parent r = node.getScene().getRoot();
        return r instanceof StackPane sp ? sp : null;
    }
}
