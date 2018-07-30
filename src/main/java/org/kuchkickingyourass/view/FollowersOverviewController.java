package org.kuchkickingyourass.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.kuchkickingyourass.model.User;
import org.kuchkickingyourass.service.InstagramService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FollowersOverviewController implements Initializable {

    @FXML
    private ListView<User> userList;

    private final ObservableList<User> data = FXCollections.observableArrayList();

    public void updateData() {
        InstagramService service = InstagramService.getInstance();
        try {
            List<InstagramUserSummary> instagramUsers = service.getNonReciprocalFollowings();
            data.clear();
            data.addAll(instagramUsers.stream().map(User::new).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateData();
        userList.setCellFactory(param -> new UserWidgetCell());
        userList.setItems(data);
    }

    class UserWidgetCell extends ListCell<User> {
        @Override
        protected void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);
            Optional.ofNullable(user).ifPresent(u -> {
                UserWidgetController controller = new UserWidgetController();
                controller.init(user);
                setGraphic(controller.getView());
            });
        }
    }
}