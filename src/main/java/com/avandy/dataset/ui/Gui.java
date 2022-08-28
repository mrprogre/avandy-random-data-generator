package com.avandy.dataset.ui;

import com.avandy.dataset.generator.Generator;
import com.avandy.dataset.util.Saver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Objects;

public class Gui extends JFrame {
    public static DefaultTableModel model;
    public static final String[] MAIN_TABLE_HEADERS = {"№", "Int", "Long", "Name", "Age", "Avg grade",
            "Car", "Color", "Country", "Orders", "Sales", "Last order"};
    public static JComboBox<String> saveFormatComboBox;
    private static final Font GUI_FONT = new Font("Tahoma", Font.PLAIN, 14);
    private static final String[] SAVE_FORMAT = new String[]{"csv", "txt"};
    private final JTextField rowsCount;
    private static JLabel statusLabel;
    private static final int BUTTON_WIDTH = 36;
    // Icons
    private static final ImageIcon LOGO_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/logo.png")));
    private static final ImageIcon CLEAR_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/clear.png")));
    private static final ImageIcon WHEN_MOUSE_ON_CLEAR_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/clear2.png")));
    private static final ImageIcon EXPORT_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/export.png")));
    private static final ImageIcon WHEN_MOUSE_ON_EXPORT_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/export2.png")));
    private static final ImageIcon START_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/start.png")));
    private static final ImageIcon WHEN_MOUSE_ON_START_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/start2.png")));
    private static final ImageIcon STOP_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/stop.png")));
    private static final ImageIcon WHEN_MOUSE_ON_STOP_BUTTON_ICON = new ImageIcon(Toolkit.getDefaultToolkit()
            .createImage(Gui.class.getResource("/icons/stop2.png")));

    public Gui() {
        setResizable(false);
        getContentPane().setBackground(new Color(233, 223, 178));
        setContentPane(new Background());
        setTitle("avandy-random-data-generator");
        setIconImage(LOGO_ICON.getImage());
        setFont(GUI_FONT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(370, 190, 1195, 600);
        getContentPane().setLayout(null);

        //Table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 40, 1160, 500);
        getContentPane().add(scrollPane);
        model = new DefaultTableModel(new Object[][]{
        }, MAIN_TABLE_HEADERS) {
            final boolean[] columnEditable = new boolean[]{
                    false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditable[column];
            }

            // Сортировка
            final Class[] types_unique = {Integer.class, Integer.class, Long.class, String.class, Byte.class,
                    Double.class, String.class, String.class, String.class, Byte.class, Double.class, LocalDate.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types_unique[columnIndex];
            }
        };
        JTable table = new JTable(model);
        //headers
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 13));
        //Cell alignment
        DefaultTableCellRenderer rendererCenter = new DefaultTableCellRenderer();
        rendererCenter.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(rendererCenter);
        table.getColumnModel().getColumn(4).setCellRenderer(rendererCenter);
        table.getColumnModel().getColumn(5).setCellRenderer(rendererCenter);
        table.getColumnModel().getColumn(9).setCellRenderer(rendererCenter);
        table.getColumnModel().getColumn(11).setCellRenderer(rendererCenter);

        DefaultTableCellRenderer rendererLeft = new DefaultTableCellRenderer();
        rendererLeft.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(1).setCellRenderer(rendererLeft);
        table.getColumnModel().getColumn(2).setCellRenderer(rendererLeft);
        table.getColumnModel().getColumn(3).setCellRenderer(rendererLeft);
        table.getColumnModel().getColumn(10).setCellRenderer(rendererLeft);

        table.setRowHeight(28);
        table.setColumnSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setFont(GUI_FONT);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(1).setMaxWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setMaxWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setMaxWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setMaxWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.setAutoCreateRowSorter(true);
        scrollPane.setViewportView(table);

        /* TOP LEFT */
        int topLeftX = 10;
        int topLeftY = 10;

        // Количество строк для генерации
        rowsCount = new JTextField();
        rowsCount.setBounds(topLeftX, topLeftY, 56, 22);
        getContentPane().add(rowsCount);
        rowsCount.setText("1000");

        // Создание строк
        JButton generateButton = new JButton();
        generateButton.setToolTipText("generate data");
        generateButton.setIcon(START_BUTTON_ICON);
        generateButton.setBounds(topLeftX + 70, topLeftY, BUTTON_WIDTH, 22);
        generateButton.setFocusable(false);
        generateButton.setContentAreaFilled(false);
        generateButton.setBorderPainted(false);
        getContentPane().add(generateButton);

        generateButton.addActionListener(e -> {
            long rows = Long.parseLong(rowsCount.getText());
            if (rows > 1_000_000) {
                rows = 1_000_000;
                rowsCount.setText(String.valueOf(rows));
            }
            new Generator().generate(rows);
        });

        generateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                generateButton.setIcon(WHEN_MOUSE_ON_START_BUTTON_ICON);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                generateButton.setIcon(START_BUTTON_ICON);
            }
        });

        // Статус
        statusLabel = new JLabel();
        statusLabel.setBounds(topLeftX + 110, topLeftY + 4, 300, 14);
        getContentPane().add(statusLabel);

        /* TOP RIGHT */
        int topRightX = 990;
        int topRightY = 10;

        // Формат выгрузки
        saveFormatComboBox = new JComboBox<>();
        saveFormatComboBox.setBounds(topRightX, topRightY, 55, 22);
        saveFormatComboBox.setBackground(new Color(238, 238, 238));
        saveFormatComboBox.setFont(new Font("Tahoma", Font.BOLD, 12));
        saveFormatComboBox.setEditable(false);
        saveFormatComboBox.setModel(new DefaultComboBoxModel<>(SAVE_FORMAT));
        this.getContentPane().add(saveFormatComboBox);

        // Выгрузка в файл
        JButton exportButton = new JButton();
        exportButton.setToolTipText("export data");
        exportButton.setIcon(EXPORT_BUTTON_ICON);
        exportButton.setFocusable(false);
        exportButton.setContentAreaFilled(false);
        exportButton.setBorderPainted(false);
        exportButton.setBounds(topRightX + 60, topRightY, BUTTON_WIDTH, 22);
        getContentPane().add(exportButton);
        exportButton.addActionListener(e -> {
            if (model.getRowCount() > 0) {
                JFileChooser saver = new JFileChooser();
                File file = new File(System.getProperty("user.home") +
                        System.getProperty("file.separator") + "Desktop");
                saver.setCurrentDirectory(file);
                int res = saver.showDialog(null, "Save");
                if (res == JFileChooser.APPROVE_OPTION) {
                    //new Saver().exportData(saver);
                    new Thread(() -> {
                        try {
                            new Saver().exportData(saver);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
            } else {
                setStatus("No data to export");
            }
        });

        exportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exportButton.setIcon(WHEN_MOUSE_ON_EXPORT_BUTTON_ICON);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exportButton.setIcon(EXPORT_BUTTON_ICON);
            }
        });

        // Остановка экспорта строк
        JButton stopGeneration = new JButton();
        stopGeneration.setToolTipText("export stop");
        stopGeneration.setIcon(STOP_BUTTON_ICON);
        stopGeneration.setFocusable(false);
        stopGeneration.setContentAreaFilled(false);
        stopGeneration.setBorderPainted(false);
        stopGeneration.setBounds(topRightX + 100, topRightY, BUTTON_WIDTH, 22);
        getContentPane().add(stopGeneration);

        stopGeneration.addActionListener(e -> {
            Saver.isExportStop.set(true);
            setStatus("Stopped");
        });

        stopGeneration.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                stopGeneration.setIcon(WHEN_MOUSE_ON_STOP_BUTTON_ICON);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                stopGeneration.setIcon(STOP_BUTTON_ICON);
            }
        });

        // Очистка модели
        JButton clearButton = new JButton();
        clearButton.setToolTipText("clear table");
        clearButton.setIcon(CLEAR_BUTTON_ICON);
        clearButton.setFocusable(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorderPainted(false);
        clearButton.setBounds(topRightX + 143, topRightY, BUTTON_WIDTH, 22);
        getContentPane().add(clearButton);
        clearButton.addActionListener(e -> {
            if (model.getRowCount() > 0) {
                model.setRowCount(0);
            }
        });

        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearButton.setIcon(WHEN_MOUSE_ON_CLEAR_BUTTON_ICON);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                clearButton.setIcon(CLEAR_BUTTON_ICON);
            }
        });

        // Подпись
        JLabel labelSign = new JLabel("mrprogre");
        labelSign.setForeground(new Color(0, 0, 0));
        //labelSign.setEnabled(false);
        labelSign.setFont(new Font("Tahoma", Font.BOLD, 11));
        labelSign.setBounds(1113, 543, 57, 14);
        getContentPane().add(labelSign);
        labelSign.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelSign.setForeground(new Color(40, 36, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelSign.setForeground(new Color(0, 0, 0));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    String url = "https://github.com/mrprogre";
                    URI uri = null;
                    try {
                        uri = new URI(url);
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                    Desktop desktop = Desktop.getDesktop();
                    assert uri != null;
                    try {
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        setVisible(true);
    }

    // Установка статуса для информативности
    public static void setStatus(String status) {
        statusLabel.setText(status);
    }

    // Фон UI
    static class Background extends JPanel {
        public void paintComponent(Graphics g) {
            try {
                Image im = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/background/gray.png")));
                g.drawImage(im, 0, 0, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}