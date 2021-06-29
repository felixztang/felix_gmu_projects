package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestChatClientConnection.class,
    TestRoomManager.class
})

public class Suite_bar {
}