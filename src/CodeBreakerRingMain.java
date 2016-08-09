
public class CodeBreakerRingMain {

    public static void main(String[] args) {
        
        CodeBreakerRingModel model = new CodeBreakerRingModel();
        CodeBreakerRingView view = new CodeBreakerRingView(model);
        CodeBreakerRingController controller = new CodeBreakerRingController(model, view);
    }
}
