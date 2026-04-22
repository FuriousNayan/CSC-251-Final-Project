package com.wecib.ui;

import com.wecib.model.Attack;
import com.wecib.model.Card;
import com.wecib.model.CardType;
import com.wecib.util.TypeChart;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CardView extends VBox {

    public enum Size { LARGE, MEDIUM, SMALL }

    private static final double[][] BASE_DIMENSIONS = {
        {250, 370},
        {195, 285},
        {130, 170},
    };
    private double scale = 1.0;
    /** Non-null while a card is bound; used to refit the name when {@link #setScaledSize} runs. */
    private String displayedName;

    private final Size size;
    private final Label nameLabel;
    private final Label hpLabel;
    private final Label hpSuffix;
    private final ProgressBar hpBar;
    private final VBox attacksBox;
    private final StackPane imageContainer;
    private final ImageView imageView;
    private final HBox headerBar;
    private final Circle typeDot;
    private final Label typeLabel;

    public CardView() {
        this(null, Size.MEDIUM);
    }

    public CardView(Card card) {
        this(card, Size.MEDIUM);
    }

    public CardView(Card card, Size size) {
        this.size = size;
        double w = BASE_DIMENSIONS[size.ordinal()][0];
        double h = BASE_DIMENSIONS[size.ordinal()][1];
        setPrefSize(w, h);
        setMinSize(w, h);
        setMaxSize(w, h);
        getStyleClass().addAll("card-view", "card-" + size.name().toLowerCase());
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        boolean small = size == Size.SMALL;
        boolean large = size == Size.LARGE;
        double pad = small ? 5 : (large ? 8 : 7);

        headerBar = new HBox(small ? 4 : 8);
        headerBar.setAlignment(Pos.CENTER_LEFT);
        headerBar.getStyleClass().add("card-header-bar");
        headerBar.setPadding(new Insets(small ? 5 : (large ? 8 : 7), pad + 3, small ? 5 : (large ? 8 : 7), pad + 3));

        typeDot = new Circle(small ? 5 : (large ? 8 : 7));
        typeDot.getStyleClass().add("type-dot");

        nameLabel = new Label();
        nameLabel.getStyleClass().add("card-name");
        nameLabel.setMinWidth(0);
        nameLabel.setWrapText(false);
        nameLabel.setEllipsisString("");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        hpLabel = new Label();
        hpLabel.getStyleClass().add("card-hp-number");
        hpLabel.setMinWidth(Region.USE_PREF_SIZE);

        hpSuffix = new Label("HP");
        hpSuffix.getStyleClass().add("card-hp-suffix");
        hpSuffix.setMinWidth(Region.USE_PREF_SIZE);

        headerBar.getChildren().addAll(typeDot, nameLabel, hpLabel, hpSuffix);

        VBox body = new VBox(small ? 3 : (large ? 6 : 5));
        body.setPadding(new Insets(pad));
        body.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(body, Priority.ALWAYS);

        double imgHeight = small ? 70 : (size == Size.MEDIUM ? 120 : 155);
        double imgWidth = w - (pad * 2) - 14;

        imageView = new ImageView();
        imageView.setFitWidth(imgWidth);
        imageView.setFitHeight(imgHeight);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageContainer = new StackPane(imageView);
        imageContainer.getStyleClass().add("card-image-area");
        imageContainer.setPrefHeight(imgHeight);
        imageContainer.setMinHeight(imgHeight);
        imageContainer.setMaxHeight(imgHeight);
        imageContainer.setMaxWidth(Double.MAX_VALUE);

        hpBar = new ProgressBar(1.0);
        hpBar.setMaxWidth(Double.MAX_VALUE);
        hpBar.getStyleClass().add("card-hp-bar");
        hpBar.setPrefHeight(small ? 5 : 7);
        hpBar.setMaxHeight(small ? 5 : 7);

        typeLabel = new Label();
        typeLabel.getStyleClass().add("card-type-badge");

        attacksBox = new VBox(small ? 2 : (large ? 5 : 4));
        attacksBox.getStyleClass().add("card-attacks");
        attacksBox.setPadding(new Insets(small ? 2 : 4, 0, 0, 0));

        body.getChildren().addAll(imageContainer, hpBar, typeLabel, attacksBox);

        if (small) {
            attacksBox.setVisible(false);
            attacksBox.setManaged(false);
            typeLabel.setVisible(false);
            typeLabel.setManaged(false);
        }

        getChildren().addAll(headerBar, body);

        if (card != null) {
            setCard(card, null);
        }
    }

    // Method to scale the card view dynamically
    public void setScaledSize(double scale) {
        this.scale = scale;
        double[] base = BASE_DIMENSIONS[size.ordinal()];
        double w = base[0] * scale;
        double h = base[1] * scale;
        setPrefSize(w, h);
        setMinSize(w, h);
        setMaxSize(w, h);
        // Update image size
        double pad = (size == Size.SMALL ? 5 : (size == Size.LARGE ? 8 : 7)) * scale;
        double imgHeight = (size == Size.SMALL ? 70 : (size == Size.MEDIUM ? 120 : 155)) * scale;
        double imgWidth = w - (pad * 2) - 14 * scale;
        imageView.setFitWidth(imgWidth);
        imageView.setFitHeight(imgHeight);
        imageContainer.setPrefHeight(imgHeight);
        imageContainer.setMinHeight(imgHeight);
        imageContainer.setMaxHeight(imgHeight);
        // Update header bar and type dot
        headerBar.setPadding(new Insets((size == Size.SMALL ? 5 : (size == Size.LARGE ? 8 : 7)) * scale, pad + 3 * scale, (size == Size.SMALL ? 5 : (size == Size.LARGE ? 8 : 7)) * scale, pad + 3 * scale));
        typeDot.setRadius((size == Size.SMALL ? 5 : (size == Size.LARGE ? 8 : 7)) * scale);
        // Update hp bar
        hpBar.setPrefHeight((size == Size.SMALL ? 5 : 7) * scale);
        hpBar.setMaxHeight((size == Size.SMALL ? 5 : 7) * scale);
        if (displayedName != null) {
            applyCardNameTypography();
        }
    }

    /** @param matchupOpponent when non-null (e.g. in battle), attack tooltips include effectiveness vs this card */
    public void setCard(Card card, Card matchupOpponent) {
        if (card == null) {
            displayedName = null;
            nameLabel.setText("");
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            hpLabel.setText("");
            hpSuffix.setText("");
            hpBar.setProgress(0);
            attacksBox.getChildren().clear();
            imageView.setImage(null);
            typeLabel.setText("");
            Tooltip.install(typeDot, null);
            typeLabel.setTooltip(null);
            getStyleClass().removeIf(s -> s.startsWith("type-"));
            return;
        }

        displayedName = card.getName();
        applyCardNameTypography();

        hpLabel.setText(String.valueOf(card.getCurrentHp()));
        hpSuffix.setText("HP");
        typeLabel.setText(card.getType().displayName());
        updateHp(card);

        Tooltip defendTip = new Tooltip(TypeChart.defensiveHint(card.getType()));
        defendTip.setWrapText(true);
        defendTip.setMaxWidth(420);
        Tooltip.install(typeDot, defendTip);
        Tooltip.install(typeLabel, defendTip);

        getStyleClass().removeIf(s -> s.startsWith("type-"));
        getStyleClass().add("type-" + card.getType().name().toLowerCase());

        String color = getTypeColor(card.getType());
        typeDot.setStyle("-fx-fill: " + color + ";");

        if (card.getImagePath() != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream(card.getImagePath()));
                imageView.setImage(img);
            } catch (Exception e) {
                imageView.setImage(null);
            }
        } else {
            imageView.setImage(null);
        }

        attacksBox.getChildren().clear();
        for (Attack atk : card.getAttacks()) {
            HBox atkRow = new HBox(size == Size.SMALL ? 3 : 7);
            atkRow.setAlignment(Pos.CENTER_LEFT);
            atkRow.getStyleClass().add("attack-row");

            for (int e = 0; e < atk.getEnergyCost(); e++) {
                Circle energyCircle = new Circle(size == Size.SMALL ? 4 : 6);
                energyCircle.setStyle("-fx-fill: " + getTypeColor(atk.getType()) + ";");
                energyCircle.getStyleClass().add("energy-circle");
                atkRow.getChildren().add(energyCircle);
            }

            Label atkName = new Label(atk.getName());
            atkName.getStyleClass().add("attack-name");
            HBox.setHgrow(atkName, Priority.ALWAYS);
            double atkMaxW = (size == Size.SMALL ? 70 : (size == Size.MEDIUM ? 130 : 170));
            atkName.setMaxWidth(atkMaxW);
            double atkFont = size == Size.SMALL ? 10 : (size == Size.LARGE ? 13 : 12);
            fitLabelFont(atkName, atk.getName(), atkMaxW, atkFont, 6, true);

            Label atkDmg = new Label(String.valueOf(atk.getDamage()));
            atkDmg.getStyleClass().add("attack-damage");

            atkRow.getChildren().addAll(atkName, atkDmg);

            Tooltip atkTip = new Tooltip(buildAttackTooltip(atk, matchupOpponent));
            atkTip.setWrapText(true);
            atkTip.setMaxWidth(420);
            Tooltip.install(atkRow, atkTip);

            attacksBox.getChildren().add(atkRow);
        }
    }

    public void setCard(Card card) {
        setCard(card, null);
    }

    private void applyCardNameTypography() {
        if (displayedName == null || displayedName.isEmpty()) {
            nameLabel.setText("");
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            return;
        }
        nameLabel.setText(displayedName);
        double cardW = BASE_DIMENSIONS[size.ordinal()][0] * scale;
        boolean isSmall = size == Size.SMALL;
        double reserved = (isSmall ? 50 : 70) * scale;
        double nameAvail = Math.max(8, cardW - reserved - 6);
        nameLabel.setMaxWidth(nameAvail);
        double defaultFont = (isSmall ? 9.5 : (size == Size.LARGE ? 12.5 : 11)) * scale;
        double minFont = Math.max(3, 3.5 * scale);
        fitLabelFont(nameLabel, displayedName, nameAvail, defaultFont, minFont, true);
    }

    private static String buildAttackTooltip(Attack atk, Card matchupOpponent) {
        StringBuilder sb = new StringBuilder();
        if (matchupOpponent != null) {
            double mult = TypeChart.getMultiplier(atk.getType(), matchupOpponent.getType());
            int est = (int) (atk.getDamage() * mult);
            sb.append("vs ").append(matchupOpponent.getName())
                    .append(" (").append(matchupOpponent.getType().displayName()).append(")\n");
            if (mult > 1.0) {
                sb.append("Super effective — ~").append(est).append(" damage (2×)\n\n");
            } else if (mult < 1.0) {
                sb.append("Not very effective — ~").append(est).append(" damage (0.5×)\n\n");
            } else {
                sb.append("Neutral — ").append(est).append(" damage (1×)\n\n");
            }
        }
        sb.append(TypeChart.offensiveHint(atk.getType()));
        return sb.toString();
    }

    private void fitLabelFont(Label label, String text, double maxWidth, double defaultSize, double minSize, boolean bold) {
        FontWeight weight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
        double fontSize = defaultSize;
        while (fontSize > minSize) {
            Text measure = new Text(text);
            measure.setFont(Font.font("System", weight, fontSize));
            if (measure.getLayoutBounds().getWidth() <= maxWidth) break;
            fontSize -= 0.5;
        }
        String boldStr = bold ? "-fx-font-weight: bold; " : "";
        label.setStyle(boldStr + "-fx-font-size: " + fontSize + "px;");
        label.setMinWidth(0);
        label.setEllipsisString("");
        label.setWrapText(false);
    }

    public void updateHp(Card card) {
        if (card == null) return;
        hpLabel.setText(String.valueOf(card.getCurrentHp()));
        hpBar.setProgress(card.hpPercent());

        hpBar.getStyleClass().removeAll("hp-red", "hp-yellow", "hp-green");
        if (card.hpPercent() <= 0.25) {
            hpBar.getStyleClass().add("hp-red");
        } else if (card.hpPercent() <= 0.5) {
            hpBar.getStyleClass().add("hp-yellow");
        } else {
            hpBar.getStyleClass().add("hp-green");
        }
    }

    public static String getTypeColor(CardType type) {
        return switch (type) {
            case FIRE -> "#ef4444";
            case WATER -> "#3b82f6";
            case EARTH -> "#a0722a";
            case ELECTRIC -> "#eab308";
            case WIND -> "#10b981";
            case SHADOW -> "#a855f7";
            case LIGHT -> "#f59e0b";
            case AURA -> "#06b6d4";
            case DIRT -> "#92702a";
            case STEEL -> "#9ca3af";
        };
    }
}
