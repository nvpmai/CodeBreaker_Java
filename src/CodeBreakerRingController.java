
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class CodeBreakerRingController extends JFrame
{
    private CodeBreakerRingModel model;
    private CodeBreakerRingView view;
    
    public CodeBreakerRingController(CodeBreakerRingModel model, CodeBreakerRingView view) 
    {
        this.model = model;
        this.view = view;
        for (int i = 0; i < 8; i++) {
            view.addBeingFocusedListener(i, new BeingFocused(i));
        }
        view.addGiveUpListener(new GiveUp());
        view.addAcceptListener(new Accept());
        view.addRestartListener(new Restart());
        view.addQuitListener(new Quit());
        view.addChangeIconListener(new ChangeIcon());
        view.addStartTimerListener(new StartTimer());
        view.addShowScoreBoardListener(new ShowScoreboard());
    }
    
    // Restart game
    public void gameRestart() 
    {
       view.dispose();
       dispose();
       CodeBreakerRingModel model = new CodeBreakerRingModel();
       new CodeBreakerRingController(model, new CodeBreakerRingView(model)); 
    }
    
    // Display the secret code
    public void displayScecretCode()
    {
        for (int i = 0; i < 6; i++)
            view.putMarbleToHoles(i, model.getSecretCode(i) - 1);
        model.setCorrectAnswer(6);
    }
    
    // Save new score
   public void saveNewRecord() 
    {
        if (model.checkScoreboard()) 
        {
            String playerName = view.showDialogInput().replaceAll("\\p{Punct}", "");
            if (playerName != null) 
                model.savePlayerName(playerName);
        }
    }
    
    // Event is triggered when player chooses marble 
    class BeingFocused implements ActionListener 
    {
        private int index;
        public BeingFocused(int index) {
            this.index = index;
        }
        public void actionPerformed(ActionEvent e) {
            model.setCurrentChoice(index + 1);
        }
    }
    
    // When player clicks 'Accept' button
    class Accept implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            // Start a new game if player has won
            if (model.getNumberOfCorrect() == 6) 
            {
                gameRestart();
                return;
            }
            // Update number of attempts
            model.attemptsAdd();
            view.updateAttemptView();
            // Update view and model if player chooses marble
            if (model.getCurrentChoice() != 0) 
            {
                model.updateModel();
                view.updateView();
                if (model.isWin()) 
                {
                    model.setScore();
                    view.showWinningMessages();
                    saveNewRecord(); 
                    return;
                }
            }
            model.setCurrentChoice(0);
            model.switchFocus();  
            view.switchFocusView(model.getCurrentHolePosition());
        }
    }
    
    // When player clicks 'Start' button
    class Restart implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) {
            gameRestart();
        }
    }
    
    // When player clicks 'Give up' button
    class GiveUp implements ActionListener 
    {
        public void actionPerformed(ActionEvent e){
            displayScecretCode();
        }
    }
    
    class ChangeIcon implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if (view.getIconHeartStatus()) 
                view.switchIcon("");
            else 
                view.switchIcon("_heart");
        }
    }
    
    // When player clicks 'Quit' button
    class Quit implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            model.writeFile();
            view.dispose();
            dispose();
        }
    }
    
    // When player clicks 'Start timer' button: Count down the time
    class StartTimer implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            // The game must not be started when running the clock
            if (model.isStarted() || view.isTimeStarted()) 
            {
                view.showDialog("Please restart the game to play in time mode");
                return;
            }
            int seconds = view.getMinutes() * 60 + view.getSeconds();
            // Validate input
            if (seconds == 0) 
            {
                view.showDialog("Invalid input for timer");
                return;
            }
            // Set time to run
            Timer timer = new Timer(1000, new ActionListener() {
                int seconds_2 = seconds;
                public void actionPerformed(ActionEvent e) 
                {
                    // Check winnning condition
                    if (model.isWin()) 
                    {
                        ((Timer)e.getSource()).stop();
                        model.setScore();
                        view.showWinningMessages();
                        return;
                    }
                    // Time off
                    if (seconds_2 == 0) 
                    {
                        view.setTimerCountdown("TIME OFF!");
                        ((Timer)e.getSource()).stop();
                        displayScecretCode();
                    } 
                    else 
                    {
                        view.setTimerCountdown(Integer.toString(seconds_2));
                        seconds_2--;
                    }
                }
                });
            timer.start();
    }}
    
    // When player clicks "View Scoreboard"
   class ShowScoreboard implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) {
            view.showDialog(model.getScoreboard());
        }
    }
    
}
