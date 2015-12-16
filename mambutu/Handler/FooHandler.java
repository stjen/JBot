package mambutu.Handler;

/**
 * Created by stefan on 12/16/15.
 */
public class FooHandler implements Handler {

    private String handles = "foo";
    private MessageDistributor messageDistributor;

    public FooHandler(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
        messageDistributor.registerCommandHandler(this);
    }


    @Override
    public String handles() {
        return handles;
    }

    @Override
    public String handle(String command) {
        String[] commandArr = command.split("\\s+");
        System.out.println("We be handlin\'");
        return "Got ya";
    }


}
