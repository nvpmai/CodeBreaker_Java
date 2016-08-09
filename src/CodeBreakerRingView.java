import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CodeBreakerRingView extends JFrame {
    private CodeBreakerRingModel model;
    
    // Store icons (marbles, holes, status icons)
    final private ImageIcon correct_icon = new ImageIcon("images/correct.png");
    final private ImageIcon half_correct_icon = new ImageIcon("images/half_correct.png");
    final private ImageIcon not_correct_icon = new ImageIcon("images/not_correct.png");
    final private ImageIcon icon_hole = new ImageIcon("images/white.png");
    final private ImageIcon hole_outline = new ImageIcon("images/circle-outline.png");
    final private ImageIcon code_broken_icon = (new ImageIcon("images/code_broken.png"));
    private ImageIcon[] icons_color = new ImageIcon[8];
    
    // Label (holes, status, number of attemps, score, time labels)
    private JLabel[] holes = new JLabel[6];
    private JLabel[] status = new JLabel[6];
    private JLabel code_broken = new JLabel(code_broken_icon);
    private JLabel time_show = new JLabel("");
    private JLabel attempts_display = new JLabel("0");
    private JLabel score_display = new JLabel("Score", SwingConstants.CENTER);
    final private JLabel seconds_label = new JLabel("seconds");
    final private JLabel minutes_label = new JLabel("minutes");
    
    // Main panes
    private JPanel optionPane = new JPanel();
    private JPanel mainPane = new JPanel();
    private JPanel playScreen = new JPanel();
    private JPanel bigPlayScreen = new JPanel();
    private JPanel marblesPane = new JPanel();
    private JPanel statusPane = new JPanel();
    private JPanel playScreenButtonPane = new JPanel();
    private JPanel codeBrokenPane = new JPanel();
    
    // Time panes
    private JPanel time_field_pane = new JPanel();
    private JPanel time_button_pane = new JPanel();
    
    // Panes to display holes
    private JPanel top_holes = new JPanel();
    private JPanel middle_holes = new JPanel();
    private JPanel bottom_holes = new JPanel();
    
    // Buttons 
    private JButton[] marbles = new JButton[8];
    private JButton startNew_button = new JButton("RESTART");
    private JButton quit_button = new JButton("QUIT");
    private JButton accept_button = new JButton("Accept");
    private JButton giveUp_button = new JButton("Give up");
    private JButton change_icon_button = new JButton("Change icon");
    private JButton start_time_button = new JButton("Start timer");
    private JButton show_scoreBoard_button = new JButton("View Scoreboard");
    
    // Textfield for timer, player's name
    JTextField time_field_minute = new JTextField(3);
    JTextField time_field_second = new JTextField(3);
    
    // Toggle between heart and button icons
    boolean isIconHeart = false;
    
    public CodeBreakerRingView(CodeBreakerRingModel model)
    {
        this.model = model;
        // Set up hole display
        for (int i = 0; i < 6; i++) 
            holes[i] = new JLabel(icon_hole);
        holes[0].setBorder(null);
        top_holes.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 0));
        middle_holes.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 0));
        bottom_holes.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 0));
        top_holes.setBorder(new EmptyBorder(20, 0, 20, 0));
        top_holes.add(holes[0]);
        top_holes.add(holes[1]);
        middle_holes.add(holes[5]);
        middle_holes.add(holes[2]);
        bottom_holes.add(holes[4]);
        bottom_holes.add(holes[3]);
        
    // Set up main screen display
        // Holes display
        playScreen.setLayout(new BoxLayout(playScreen, BoxLayout.Y_AXIS));
        playScreen.add(top_holes);
        playScreen.add(middle_holes);
        playScreen.add(bottom_holes);
        
        // Code broken display
        codeBrokenPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        codeBrokenPane.add(code_broken);
        playScreen.add(codeBrokenPane);
        
        // Accept, give up, number of attempts, time-countdown display buttons
        playScreenButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        attempts_display.setFont(new Font("Courier", Font.BOLD, 24));
        playScreenButtonPane.add(attempts_display);
        playScreenButtonPane.add(giveUp_button);
        playScreenButtonPane.add(accept_button);
        playScreenButtonPane.add(time_show);
        playScreen.add(playScreenButtonPane);
        bigPlayScreen.setLayout(new BorderLayout());
        bigPlayScreen.add(playScreen, BorderLayout.CENTER);
        bigPlayScreen.setBackground(Color.black);
        
        // Set up status display
        statusPane.setLayout(new GridLayout(6, 1));
        statusPane.setBorder(new EmptyBorder(0, 0, 0, 20));
        bigPlayScreen.add(statusPane, BorderLayout.EAST);
        
        // Set up marbles display
        String[] color_name = {"blue", "green", "red", "yellow", "purple", "magenta", "gray", "brown"};
        for (int i = 0; i < 8; i++) 
        {
            icons_color[i] = new ImageIcon("images/" + color_name[i] + ".png");
            marbles[i] = new JButton(icons_color[i]);
            marbles[i].setOpaque(false);
            marbles[i].setContentAreaFilled(false);
            marblesPane.add(marbles[i]);
        }
        marblesPane.setBackground((Color.WHITE));
        marblesPane.setBorder(BorderFactory.createLineBorder(Color.black));
                                    
        // Set up play Screen (holes, give up button, accept_button button, number of trials) 
        mainPane.setLayout(new BorderLayout());
        mainPane.add(bigPlayScreen, BorderLayout.CENTER);
        mainPane.add(marblesPane, BorderLayout.SOUTH);
        add(mainPane, BorderLayout.CENTER);
        
        // Set up status pane
        for (int i = 0; i < 6; i++) 
        {
            status[i] = new JLabel(not_correct_icon);
            statusPane.add(status[i]);
        }
        
        // Set 'code_broken' invisible
        code_broken.setVisible(false);
        
        // Set up timer pane
        minutes_label.setFont(new Font("Courier", Font.BOLD, 16));
        seconds_label.setFont(new Font("Courier", Font.BOLD, 16));
        time_field_minute.setFont(new Font("Courier", Font.BOLD, 16));
        time_field_second.setFont(new Font("Courier", Font.BOLD, 16));
        time_field_pane.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 30));
        time_field_pane.add(time_field_minute);
        time_field_pane.add(minutes_label);
        time_field_pane.add(time_field_second);
        time_field_pane.add(seconds_label);
        
        // Set up optionPane panel(Quit, Start, Change icon, timer guide)
        optionPane.setLayout(new GridLayout(8, 1));
        optionPane.add(startNew_button);
        optionPane.add(new JLabel(new ImageIcon("images/guide.png")));
        score_display.setFont(new Font("Courier", Font.BOLD, 24));
        score_display.setBorder(BorderFactory.createLineBorder(Color.black));
        optionPane.add(score_display);
        optionPane.add(time_field_pane);
        optionPane.add(start_time_button);
        optionPane.add(change_icon_button);
        optionPane.add(show_scoreBoard_button);
        optionPane.add(quit_button);
        optionPane.setBorder(BorderFactory.createLineBorder(Color.black));
        add(optionPane, BorderLayout.EAST);
        
        // Set up screen
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        switchFocusView(0);
    }
    
    // Accessors
    public JButton getMarbles(int index)
    {
        return marbles[index];
    }
    public boolean getIconHeartStatus() 
    {
        return isIconHeart;
    }
    
    // Get minutes and seconds the player sets
    public int getMinutes() 
    {
        boolean isInteger = time_field_minute.getText().matches("^-?\\d+$");
        return (isInteger) ? Integer.parseInt(time_field_minute.getText()) : 0;
    }
    public int getSeconds() 
    {
        boolean isInteger = time_field_second.getText().matches("^-?\\d+$");
        return (isInteger) ? Integer.parseInt(time_field_second.getText()) : 0;
    }
    public boolean isTimeStarted() {
        return time_show.getText().length() != 0;
    }
    
    // Mutators
    // Put marble to hole
    public void putMarbleToHoles(int position, int marble) 
    {
        holes[position].setIcon(icons_color[marble]);
    }
    
    // Change status display based on the number of correct, incorrect answers
    public void setStatus(int correct, int half_correct) 
    {
        for (int i = 0; i < correct; i++)
            status[i].setIcon(correct_icon);
        for (int i = correct; i < correct + half_correct; i++) 
            status[i].setIcon(half_correct_icon);
        for (int i = correct + half_correct; i < 6; i++)
            status[i].setIcon(not_correct_icon);
    }
    
    // Update the view after clicking "Accept" button
    public void updateView() 
    {
        putMarbleToHoles(model.getCurrentHolePosition(), model.getCurrentChoice() - 1);
        setStatus(model.getNumberOfCorrect(), model.getNumberOfHalfCorrect());
    }
    public void updateAttemptView() {
        attempts_display.setText(model.getAttempts() + "");
    }
    
    
    // Switch focused hole after the player presses Accept
    public void switchFocusView(int index) 
    {
        Border border = BorderFactory.createRaisedBevelBorder();
        Border no_border = new EmptyBorder(0, 0, 0, 0);
        if (index != 0) 
            holes[index - 1].setBorder(no_border);
        else
            holes[5].setBorder(no_border);
        holes[index].setBorder(border);
    }
    
    public void toggleIconStatus() 
    {
        isIconHeart = (!isIconHeart);
    }
    
    public void switchIcon(String nameIcon) 
    {
        String[] color_name = {"blue", "green", "red", "yellow", "purple", "magenta", "gray", "brown"};
        for (int i = 0; i < 8; i++) 
        {
            icons_color[i] = new ImageIcon("images/" + color_name[i] + nameIcon + ".png");
            marbles[i].setIcon(icons_color[i]);
        }
        toggleIconStatus();
        updateAfterSwitching();
    }
    
    // Update icons on hole after switching icons
    
    public void updateAfterSwitching() 
    {
        for (int i = 0; i < 6; i++) 
        {
            if (model.getPlayerChoice(i) != 0)
                putMarbleToHoles(i, model.getPlayerChoice(i) - 1);
        }
    }
    
    // Show winning message
    public void showWinningMessages() 
    {
        code_broken.setVisible(true);
        score_display.setText("Score: " + model.getScore());
    }
    
    // Set text for timer countdown
    public void setTimerCountdown(String str) 
    {
        time_show.setText(str);
        time_show.setFont(new Font("Courier", Font.BOLD, 24));
    }
    
    // Show dialog message
    public void showDialog(String message) 
    {
        JOptionPane.showMessageDialog(this, message);
    } 
    
    // Show dialog that prompt users to enter input
    public String showDialogInput() 
    {
        return JOptionPane.showInputDialog(this, "Congratulation! You have made a new record\n"
                + "Please enter your name");
    }
    
    // Add listeners to buttons
    void addAcceptListener(ActionListener listenerForAcceptButton) 
    {
        accept_button.addActionListener(listenerForAcceptButton);
    }
    void addRestartListener(ActionListener listenerForRestartButton) 
    {
        startNew_button.addActionListener(listenerForRestartButton);
    }
    void addQuitListener(ActionListener listenerForQuitButton) 
    {
        quit_button.addActionListener(listenerForQuitButton);
    }
    void addChangeIconListener(ActionListener listenerForChangeIconButton) 
    {
        change_icon_button.addActionListener(listenerForChangeIconButton);
    }
    void addGiveUpListener(ActionListener listenerForGiveUpButton) 
    {
        giveUp_button.addActionListener(listenerForGiveUpButton);
    }
    void addStartTimerListener(ActionListener listenerForStartTimerButton) 
    {
        start_time_button.addActionListener(listenerForStartTimerButton);
    }
    void addBeingFocusedListener(int button_index, ActionListener listenerForBeingFocusedButton) 
    {
        marbles[button_index].addActionListener(listenerForBeingFocusedButton);
    }
    void addShowScoreBoardListener(ActionListener listenerForShowScoreBoardButton) 
    {
        show_scoreBoard_button.addActionListener(listenerForShowScoreBoardButton);
    }
    
    
}