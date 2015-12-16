package mambutu.Handler;

/**
 * Created by stefan on 12/16/15.
 */
public class BarHandler implements Handler {

    private String handles = "bar, baz, foo";
    private MessageDistributor messageDistributor;

    public BarHandler(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
        messageDistributor.registerCommandHandler(this);
    }


    @Override
    public String handles() {
        return handles;
    }

    @Override
    public String handle(String command) {
        System.out.println("We be handlin\'");
        return "got dat heavy " + command;
    }


}
