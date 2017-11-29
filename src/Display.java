import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Michał Wrzesień on 2016-05-05.
 */
public class Display extends JFrame
{
    private static final long serialVersionUID = 1L;
    private Board panel_1 = null;
    private JPanel contentPane;
    private String[] pedzle = new String[] {"Moore", "von Neumann", "Hexagonal left", "Hexagonal right", "Hexagonal random", "Pentagonal random"};
    private JCheckBoxMenuItem chckbxmntmPamitajRozmieszczenieZiaren;
    private JCheckBoxMenuItem chckbxmntmCzyPlanszePrzed;
    private JRadioButtonMenuItem rdbtnmntmRwnomierne;
    private JRadioButtonMenuItem rdbtnmntmLosowepromieKoa;
    private JRadioButtonMenuItem rdbtnmntmLosowePrzypadkowe;
    private ButtonGroup roz_zarodkow;
    private ButtonGroup bg_periodycznosc;
    private JSpinner spinner_1;

    private boolean oneGrainBoundaryExtender = false;

    public Display()
    {
        System.out.println("Display::Display();");
        setForeground(UIManager.getColor("Button.highlight"));
        setBackground(Color.WHITE);
        setTitle("Modelowanie Wielkoskalowe - Grain growth");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 700);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnPlik = new JMenu("File");
        menuBar.add(mnPlik);

        JMenuItem mntmZamknij = new JMenuItem("Close");
        mntmZamknij.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });

        chckbxmntmPamitajRozmieszczenieZiaren = new JCheckBoxMenuItem("Remember arrangement of grains");
        chckbxmntmPamitajRozmieszczenieZiaren.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Core.Config.menuSaveMap = !Display.this.chckbxmntmPamitajRozmieszczenieZiaren.isSelected();
            }
        });

        chckbxmntmCzyPlanszePrzed = new JCheckBoxMenuItem("Clean board before start");
        chckbxmntmCzyPlanszePrzed.setSelected(true);
        chckbxmntmCzyPlanszePrzed.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Core.Config.menuClean = !Display.this.chckbxmntmCzyPlanszePrzed.isSelected();
            }
        });
        mnPlik.add(chckbxmntmCzyPlanszePrzed);
        mnPlik.add(chckbxmntmPamitajRozmieszczenieZiaren);
        mnPlik.add(mntmZamknij);

        JMenu mnRozmieszczenieZarodkw = new JMenu("Arrangement of nucleons");
        menuBar.add(mnRozmieszczenieZarodkw);

        rdbtnmntmRwnomierne = new JRadioButtonMenuItem("evenly");
        rdbtnmntmRwnomierne.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                Core.Config.rozmieszczenie = 1;
            }
        });
        mnRozmieszczenieZarodkw.add(rdbtnmntmRwnomierne);

        rdbtnmntmLosowepromieKoa = new JRadioButtonMenuItem("random (circle radius)");
        rdbtnmntmLosowepromieKoa.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                Core.Config.rozmieszczenie = 2;
            }
        });
        rdbtnmntmLosowepromieKoa.setSelected(true);
        mnRozmieszczenieZarodkw.add(rdbtnmntmLosowepromieKoa);

        rdbtnmntmLosowePrzypadkowe = new JRadioButtonMenuItem("randomly choosen");
        rdbtnmntmLosowePrzypadkowe.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                Core.Config.rozmieszczenie = 3;
            }
        });
        mnRozmieszczenieZarodkw.add(rdbtnmntmLosowePrzypadkowe);



        JRadioButtonMenuItem rdbtnmntmLosoweZPromieniem = new JRadioButtonMenuItem("random with radius");
        rdbtnmntmLosoweZPromieniem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Core.Config.rozmieszczenie = 4;
            }
        });
        mnRozmieszczenieZarodkw.add(rdbtnmntmLosoweZPromieniem);


        roz_zarodkow = new ButtonGroup();
        roz_zarodkow.add(rdbtnmntmRwnomierne);
        roz_zarodkow.add(rdbtnmntmLosowepromieKoa);
        roz_zarodkow.add(rdbtnmntmLosowePrzypadkowe);
        roz_zarodkow.add(rdbtnmntmLosoweZPromieniem);

        //===========================
        JMenu mnPeriodyczno = new JMenu("Periodicity");
        menuBar.add(mnPeriodyczno);

        JRadioButtonMenuItem rdbtnmntmTak = new JRadioButtonMenuItem("Yes");
        rdbtnmntmTak.setSelected(true);
        rdbtnmntmTak.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Core.Config.bc = 0;
            }
        });
        mnPeriodyczno.add(rdbtnmntmTak);

        JRadioButtonMenuItem rdbtnmntmNie = new JRadioButtonMenuItem("No");
        rdbtnmntmNie.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Core.Config.bc=1;
            }
        });
        mnPeriodyczno.add(rdbtnmntmNie);


        bg_periodycznosc = new ButtonGroup();
        bg_periodycznosc.add(rdbtnmntmTak);
        bg_periodycznosc.add(rdbtnmntmNie);

        //================================================
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);

        JPanel panel_2 = new JPanel();
        contentPane.add(panel_2, BorderLayout.SOUTH);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("KeyPressed: "+e.getKeyCode()+", ts="+e.getWhen());
            }
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("keyReleased: "+e.getKeyCode()+", ts="+e.getWhen());
            }
        });

        JLabel lblPdzel = new JLabel("Neighborhood:");
        panel.add(lblPdzel);

        final JComboBox comboBox_1 = new JComboBox();
        comboBox_1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent argP) {
                Core.Config.sasiedztwo = comboBox_1.getSelectedIndex();
            }
        });
        comboBox_1.setBackground(new Color(255, 255, 255));
        comboBox_1.setModel(new DefaultComboBoxModel(this.pedzle));
        panel.add(comboBox_1);

        // =================================	generuj grafike 	================================

        panel_1 = new Board();
        panel_1.setBackground(SystemColor.text);
        contentPane.add(panel_1, BorderLayout.CENTER);

        new Thread(panel_1).start();

        // =================================	akcja generowania 	================================
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel_1.refresh();
            }
        });

        JButton btnGeneruj = new JButton("START");
        btnGeneruj.setBackground(Color.GREEN);
        btnGeneruj.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Core.Config.menuClean) Display.this.panel_1.clean();
                if (Core.Config.menuSaveMap) {
                    if (!Core.pkts.isEmpty()) {
                        Display.this.panel_1.clean();
                        Display.this.panel_1.loadMap();
                    } else {
                        Display.this.panel_1.random();
                        Display.this.panel_1.saveMap();
                    }
                } else {
                    Display.this.panel_1.random();
                }
                Display.this.panel_1.refresh();
                Core.Config.StatusStart = 1;
            }
        });

        JLabel label_1 = new JLabel(" ");
        panel.add(label_1);

        JLabel lblIloZiaren = new JLabel("Grains:");
        panel.add(lblIloZiaren);

        final JSpinner spinner = new JSpinner();
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                Core.Config.punkty = (Integer) spinner.getValue();
            }
        });
        spinner.setModel(new SpinnerNumberModel(Core.Config.punkty, 1, 360, 1));
        panel.add(spinner);

        JLabel label = new JLabel(" ");
        panel.add(label);

        JLabel lblCzas = new JLabel("Time");
        panel.add(lblCzas);

        spinner_1 = new JSpinner();
        spinner_1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                Core.Config.delay = (Integer) spinner_1.getValue();
            }
        });
        spinner_1.setModel(new SpinnerNumberModel(20, 1, 9999, 1));
        panel.add(spinner_1);

        JLabel lblMs = new JLabel("ms");
        panel.add(lblMs);

        JLabel label_2 = new JLabel(" ");
        panel.add(label_2);
        panel.add(btnGeneruj);

        JButton btnKoniec = new JButton("STOP");
        btnKoniec.setBackground(Color.RED);
        btnKoniec.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                Core.Config.StatusStart = 0;
                oneGrainBoundaryExtender = false;
            }
        });
        panel.add(btnKoniec);

        JButton btnReset = new JButton("RESET");
        btnReset.setBackground(Color.BLACK);
        btnReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Display.this.panel_1.clean();
                Display.this.panel_1.refresh();
                oneGrainBoundaryExtender = false;
            }
        });
        panel.add(btnReset);

        //export to JPG, PNG
        JButton btnPaint = new JButton("Export to BMP");
        btnPaint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = new BufferedImage(Display.this.panel_1.getWidth(), Display.this.panel_1.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                Display.this.panel_1.printAll(g);
                g.dispose();
                try {
                    System.out.println("try");

                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(Display.this) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        ImageIO.write(image, "bmp", file);
                    }

                    //ImageIO.write(image, "bmp", new File("Paint.bmp"));
                    //ImageIO.write(image, "png", new File("Paint.png"));
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
        });
        panel.add(btnPaint);


        JButton btnExportTxt = new JButton("Export to TXT");
        btnExportTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Display.this.panel_1.exportToFile();
            }
        });
        panel.add(btnExportTxt);

        JButton btnImportTxt = new JButton("Import from TXT");
        btnImportTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Display.this.panel_1.importFromFile();
                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
                Core.Config.StatusStart = 1;
            }
        });
        panel.add(btnImportTxt);


        JButton btnInclusionsAfter = new JButton("Inclusions AfterOnBG");
        btnInclusionsAfter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               //!!zrobic
                Display.this.panel_1.addInclusionsAfter();

                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnInclusionsAfter);

        JButton btnInclusionsBefore = new JButton("Inclusions Before");
        btnInclusionsBefore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //!!zrobic
                Display.this.panel_1.random();
                Display.this.panel_1.saveMap();

                Display.this.panel_1.refresh();

                Core.Config.StatusStart = 1;
                Display.this.panel_1.saveMap();

                Display.this.panel_1.refresh();

                Display.this.panel_1.addInclusionsBefore();

                Display.this.panel_1.refresh();
                Core.Config.StatusStart = 0;

                //Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnInclusionsBefore);

        JButton btnTest = new JButton("Test");
        btnTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Display.this.panel_1.refresh();
                //Display.this.panel_1.loadMap();
                Display.this.panel_1.loadSavedMap();
                Core.Config.StatusStart = 1;
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnTest);

        JButton btnInclusionsAfterRandom = new JButton("InclusionsAfterRandom");
        btnInclusionsAfterRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //!!zrobic
                Display.this.panel_1.addInclusionsAfterRandom();

                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnInclusionsAfterRandom);

        JButton btnSubsc1 = new JButton("Add Subsc1");
        btnSubsc1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //!!zrobic
                Display.this.panel_1.addSubstructureVer1();

                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnSubsc1);

        JButton btnRerunSubsc1 = new JButton("Rerun Subsc1");
        btnRerunSubsc1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Display.this.panel_1.clean();
                Display.this.panel_1.random();
                Display.this.panel_1.saveMap();

                Display.this.panel_1.refresh();

                //!!zrobic
                Display.this.panel_1.addRerunSubstructureVer1();
                //Core.Config.StatusStart = 1;
                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnRerunSubsc1);

        JButton btnSubsc2 = new JButton("Add Subsc2");
        btnSubsc2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //!!zrobic
                Display.this.panel_1.addSubstructureVer2();

                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnSubsc2);


        JButton btnAllGrainsBoundary = new JButton("AllGrainsBoundary");
        btnAllGrainsBoundary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //!!zrobic
                Display.this.panel_1.showOnlyBoundaries();

                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnAllGrainsBoundary);

        JButton btnOneGrainBoundary = new JButton("OneGrainBoundary");
        btnOneGrainBoundary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //!!zrobic
                if (!oneGrainBoundaryExtender)
                {
                    Display.this.panel_1.addSubstructureVer1();
                    oneGrainBoundaryExtender = true;
                }

                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();

                Display.this.panel_1.showOnlyBoundaries();

                Display.this.panel_1.loadMap();
                Display.this.panel_1.refresh();
            }
        });
        panel_2.add(btnOneGrainBoundary);
    }
}

