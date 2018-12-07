package github.lual;

import com.google.common.eventbus.EventBus;
import com.tulskiy.keymaster.common.Provider;
import github.lual.messages.types.ClientAlarmMessage;

import javax.swing.*;

public class HotkeyListener {

    public HotkeyListener(String keystroke, EventBus eventBus) {
        Provider provider = Provider.getCurrentProvider(false);
        provider.register(KeyStroke.getKeyStroke(keystroke), hotKey -> {
            eventBus.post(new ClientAlarmMessage());
        });
    }
}
