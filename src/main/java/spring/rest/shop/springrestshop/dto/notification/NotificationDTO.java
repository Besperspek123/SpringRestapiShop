package spring.rest.shop.springrestshop.dto.notification;

import lombok.Data;
import spring.rest.shop.springrestshop.dto.cart_product.CartProductDTO;
import spring.rest.shop.springrestshop.dto.user.UserDTO;
import spring.rest.shop.springrestshop.entity.Notification;
import spring.rest.shop.springrestshop.entity.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NotificationDTO {
    long id;
    String title;
    String message;
    UserDTO recipient;
    LocalDate date;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.recipient = new UserDTO(notification.getRecipientUser());
        this.date = notification.getDate();
    }
}
