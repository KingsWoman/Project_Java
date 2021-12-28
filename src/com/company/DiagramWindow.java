package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagramWindow extends JFrame {
    private final List<Human> human;
    private JPanel diagramPanel;

    public DiagramWindow(List<Human> human) {
        this.human = human;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setTitle("Diagrams");
        setLayout(null);
        add(panelButton());
        add(panelDiagram());
        setVisible(true);
    }

    private void createSexDiagram() throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (int i = 0; i < human.size(); i++) {
            if (dataset.getKeys().contains(human.get(i).getSex())) {
                dataset.setValue(human.get(i).getSex(), dataset.getValue(human.get(i).getSex()).intValue() + 1);
            } else dataset.setValue(human.get(i).getSex(), 1);
        }

        JFreeChart chart = ChartFactory.createPieChart("Половая структура по курсу", dataset, true, true, false);
        ChartUtils.saveChartAsPNG(new File("image/sexDiagram.png"), chart, 600, 400);
    }

    private void createCityDiagram() throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (int i = 0; i < human.size(); i++) {
            if (dataset.getKeys().contains(human.get(i).getCity())) {
                dataset.setValue(human.get(i).getCity(), dataset.getValue(human.get(i).getCity()).intValue() + 1);
            } else dataset.setValue(human.get(i).getCity(), 1);
        }

        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < dataset.getKeys().size(); i++) {
            result.put(dataset.getKeys().get(i), dataset.getValue(dataset.getKeys().get(i)).intValue());
        }


        JFreeChart chart = ChartFactory.createPieChart("Города студентов записанных на курс", dataset, true, true, false);
        ChartUtils.saveChartAsPNG(new File("image/cityDiagram.png"), chart, 600, 400);
    }

    private void createAgeDiagram() throws IOException {
        List<String> years = new ArrayList<>();
        for (int i = 0; i < human.size(); i++) {
            if (human.get(i).getBirthday().split("\\.").length == 3) {
                years.add(human.get(i).getBirthday().split("\\.")[2].trim());
            }
        }

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (int i = 0; i < years.size(); i++) {
            if (dataset.getKeys().contains(years.get(i))) {
                dataset.setValue(years.get(i), dataset.getValue(years.get(i)).intValue() + 1);
            } else dataset.setValue(years.get(i), 1);
        }

        JFreeChart chart = ChartFactory.createPieChart("Возраста студентов записанных на курс", dataset, true, true, false);
        ChartUtils.saveChartAsPNG(new File("image/ageDiagram.png"), chart, 600, 400);
    }

    private JPanel panelDiagram() {
        diagramPanel = new JPanel(null);
        diagramPanel.setBounds(550, 0, 600, 650);
        diagramPanel.setBackground(Color.GRAY);


        JLabel label_1 = new JLabel("Диаграмма");
        label_1.setFont(new Font("Arial", Font.BOLD, 40));
        label_1.setForeground(Color.WHITE);


        diagramPanel.add(label_1).setBounds(160, 25, 300, 40);
        return diagramPanel;
    }

    private JPanel panelButton() {
        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, 550, 650);
        panel.setBackground(Color.BLACK);
        int width_buttons = 480;
        int height_buttons = 60;
        Font font_buttons = new Font("Arial", Font.BOLD, 18);

        JButton button_3 = new JButton("Города студентов записанных на курс");
        button_3.setBounds(30, 150, width_buttons, height_buttons);
        button_3.setFocusable(false);
        button_3.setFont(font_buttons);
        button_3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button_3.addActionListener(e -> {
            try {
                createCityDiagram();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (diagramPanel.getComponents().length > 1) {
                diagramPanel.remove(1);
            }
            ImageIcon image = new ImageIcon("image/cityDiagram.png");
            JLabel label = new JLabel(image);
            diagramPanel.add(label).setBounds(0, -25, 550, 650);
            diagramPanel.repaint();
        });

        JButton button_4 = new JButton("Возраста студентов записанных на курс");
        button_4.setBounds(30, 270, width_buttons, height_buttons);
        button_4.setFocusable(false);
        button_4.setFont(font_buttons);
        button_4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button_4.addActionListener(e -> {
            try {
                createAgeDiagram();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (diagramPanel.getComponents().length > 1) {
                diagramPanel.remove(1);
            }
            ImageIcon image = new ImageIcon("image/AgeDiagram.png");
            JLabel label = new JLabel(image);
            diagramPanel.add(label).setBounds(0, -25, 550, 650);
            diagramPanel.repaint();
        });

        JButton button_5 = new JButton("Половая структура по курсу");
        button_5.setBounds(30, 390, width_buttons, height_buttons);
        button_5.setFocusable(false);
        button_5.setFont(font_buttons);
        button_5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button_5.addActionListener(e -> {
            try {
                createSexDiagram();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (diagramPanel.getComponents().length > 1) {
                diagramPanel.remove(1);
            }
            ImageIcon image = new ImageIcon("image/sexDiagram.png");
            JLabel label = new JLabel(image);
            diagramPanel.add(label).setBounds(0, -25, 550, 650);
            diagramPanel.repaint();
        });

        panel.add(button_3);
        panel.add(button_4);
        panel.add(button_5);

        return panel;
    }
}
