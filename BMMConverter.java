import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

class BMMConverter {
  private note[] notes = new note[35];
  private Sequencer player;
  private Sequence seq;
  private Track track;
  private int currentBeat = 0;
  private JButton convert;
  private JButton playButton;
  private JButton stopButton;
  private JFrame frame;
  private JTextArea text;
  private JTextField textField1;
  private JTextField textField2;
  private boolean loaded;

  public void givedata(String data) {
    String[] result = data.split(" ");
	  boolean b = result[1].contains("#");
	  
    if (b)
	    result[1] = result[1].replace( '#', 's' );
 
	  boolean b2 = result[2].contains("#");
	
    if (b)
	    result[2] = result[2].replace( '#', 's' );
 
	  System.out.println(result[0]);
	  System.out.println(result[1]);
	  System.out.println(result[2]);
	
	  buildMidi(result[0], result[1], result[2]);
  }
  
  void buildnotess() {
    
	  notes[0] = new note("C3", 48);
	  notes[1] = new note("Cs3", 49);
	  notes[2] = new note("D3", 50);
	  notes[3] = new note("Ds3", 51);
	  notes[4] = new note("E3", 52);
	  notes[4] = new note("F3", 53);
	  notes[5] = new note("Fs3", 54);
	  notes[6] = new note("G3", 55);
	  notes[7] = new note("Gs3", 56);
	  notes[8] = new note("A3", 57);
	  notes[9] = new note("As3", 58);
	  notes[10] = new note("B3", 59);
	
	  notes[11] = new note("C4", 60);
	  notes[12] = new note("Cs4", 61);
	  notes[13] = new note("D4", 62);
	  notes[14] = new note("Ds4", 63);
	  notes[15] = new note("E4", 64);
	  notes[16] = new note("F4", 65);
	  notes[17] = new note("Fs4", 66);
	  notes[18] = new note("G4", 67);
	  notes[19] = new note("Gs4", 68);
	  notes[20] = new note("A4", 69);
	  notes[21] = new note("As4", 70);
	  notes[22] = new note("B4", 71);
	
	  notes[23] = new note("C5", 72);
	  notes[24] = new note("Cs5", 73);
	  notes[25] = new note("D5", 74);
	  notes[26] = new note("Ds5", 75);
	  notes[27] = new note("E5", 76);
	  notes[28] = new note("F5", 77);
	  notes[29] = new note("Fs5", 78);
	  notes[30] = new note("G5", 79);
	  notes[31] = new note("Gs5", 80);
	  notes[32] = new note("A5", 81);
	  notes[33] = new note("As5", 82);
	  notes[34] = new note("B5", 83);
	
	text.append("Virtual keyboard initialized\n");
  }
  
  void buildSequencer() {
    try {
	    player = MidiSystem.getSequencer();
	    seq = new Sequence(Sequence.PPQ, 4);
	    track = seq.createTrack();
	    player.open();
  	 	  
	    text.append("Sequencer Initialized\n\n"); 
	    text.append("Ready to load BMM file\n");
	  } 
    catch (Exception ex) {
	    text.append("Failed to initlialize sequencer\n");
	  }
  }
  
  void refreshSequencer() {
    try {
	    player.close();
	    seq = new Sequence(Sequence.PPQ, 4);
	    track = seq.createTrack();
	    player.open();
	  } 
    catch (Exception ex) {
	    text.append("Error: Failed to refresh sequencer\n");
	  }
  }	
  
	

  public void buildMidi(String beat, String note1, String note2) {

	  // Get the beat
	  int bt = 0;
	  if (beat.equals("1/16"))
	    bt = 1;
	  else if (beat.equals("1/8"))
	    bt = 2;
	  else if (beat.equals("Quarter"))
	    bt = 4;
	  else if (beat.equals("Half"))
	    bt = 16;
	  else if (beat.equals("Whole"))
	    bt = 32;
	
	  currentBeat = currentBeat + bt;
	
    // Note handling
    int note1num = 0;
    int note2num = 0;	
	  for (note currentNote : notes) {
	    
      if (note1.equals(currentNote.getName()))
          note1num = currentNote.getNumber();

	    if (note2.equals(currentNote.getName()))
	      note2num = currentNote.getNumber();	
	  }
	    
	  try {
	    track.add(easymidi.makeevent(144, 1, note1num, 100, currentBeat));
	    track.add(easymidi.makeevent(144, 2, note2num, 100, currentBeat));
	    
	    track.add(easymidi.makeevent(128, 1, note1num, 100, currentBeat + bt));
	    track.add(easymidi.makeevent(128, 2, note2num, 100, currentBeat + bt));
	  } 
    catch (Exception ex) {
        text.append("Error: Failed to create MIDI\n");
    }	  
  }
    
  public void convert() {
    try {
	    File outputFile = new File("output.midi");
	    int[] fileTypes = MidiSystem.getMidiFileTypes(seq);
	    MidiSystem.write(seq, fileTypes[0], outputFile);
	    text.append("MIDI exported to program's directory\n");
    } 
    catch (Exception ex) {
        ex.printStackTrace();
    }
  }
    
  public void play() {
    
    //set instruments
	  int tf1 = Integer.parseInt(textField1.getText());
	  int tf2 = Integer.parseInt(textField2.getText());
	
	  track.add(easymidi.makeevent(192, 1, tf1, 0, 1));
	  track.add(easymidi.makeevent(192, 2, tf2, 0, 1));
	
	  try {
	    player.setSequence(seq);
	    player.start();
	    
	  } 
    catch (Exception ex) {
	    ex.printStackTrace();
	  }
  }

  public void read(File file) {
    if (file == null)
      return;

    text.append("Converting file to MIDI\n");
	  try {
	    FileReader fileReader = new FileReader(file);
	    BufferedReader reader = new BufferedReader (fileReader);
	    String line = null;
	  
	    while ((line = reader.readLine()) !=null) {
	      System.out.println(line);
		    if (line.contains("1/") || line.contains("Whole") || line.contains("Quarter"))
          givedata(line);
      }
	    reader.close();
	    text.append("Process Complete\n");
	    loaded = true;	  
	  } 
    catch (Exception ex) {
	    ex.printStackTrace();
	  }   
  }  
  
  public void buildGUI() {

    convert = new JButton("Export");
	  convert.addActionListener(new convertListener());
	  playButton = new JButton("Play");
	  playButton.addActionListener(new playListener());
	  stopButton = new JButton("Stop");
	  stopButton.addActionListener(new stopListener());
	  JPanel lpanel = new JPanel();
	  lpanel.add(convert);
	  lpanel.add(playButton);
	  lpanel.add(stopButton);
	
	  JLabel label = new JLabel("Instrument 1");
	  JLabel label2 = new JLabel("Instrument 2");
	  textField1 = new JTextField(5);
	  textField2 = new JTextField(5);
	  textField1.setText("1");
	  textField2.setText("1");
	  JPanel tpanel = new JPanel();
	  tpanel.add(label);
	  tpanel.add(textField1);
	  tpanel.add(label2);
	  tpanel.add(textField2);	
	
	  JMenuBar menubar = new JMenuBar();
	  JMenu filemenu = new JMenu("File");
	  JMenuItem openmenu = new JMenuItem("Open");
	  openmenu.addActionListener(new openListener());
	  filemenu.add(openmenu);
	  menubar.add(filemenu);
	
	  text = new JTextArea(10,20);
	  text.setEditable(false);
	  text.setLineWrap(true);
	  JScrollPane scroller = new JScrollPane(text);
	  scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	  scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	  JPanel panelc = new JPanel();
	  panelc.add(scroller);
	
    frame = new JFrame();
    frame.setJMenuBar(menubar);
	  frame.setVisible(true);
	  frame.setSize(300,300);
	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  frame.getContentPane().add(BorderLayout.SOUTH, lpanel);
	  frame.getContentPane().add(BorderLayout.CENTER, panelc);
	  frame.getContentPane().add(BorderLayout.NORTH, tpanel);
	  frame.setTitle("BMM to MIDI Converter");
  }
  
  class openListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
	    JFileChooser fileopen = new JFileChooser();
	    fileopen.showOpenDialog(frame);
	    currentBeat = 0;
	    refreshSequencer();
	    read(fileopen.getSelectedFile());
	  }
  }

  class convertListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      if (loaded)
	      convert();
	    else
        text.append("How about we load a file first?\n");		
	  }	
  }	

  class playListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
	    if (loaded)
	      play();
	    else
	      text.append("How about we load a file first?\n");
    }
  }
  class stopListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
	    player.stop();
	    player.setTickPosition(0);
	  } 
  }
  
  public static void main(String[] args) {
    bmmconvert BMM = new bmmconvert();
	  BMM.buildGUI();
	  BMM.buildnotess();
	  BMM.buildSequencer();	
  }
}
