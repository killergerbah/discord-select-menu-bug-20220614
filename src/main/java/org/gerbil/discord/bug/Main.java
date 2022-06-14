package org.gerbil.discord.bug;

import com.typesafe.config.ConfigFactory;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;

public class Main {

    public static void main(String... args) {
        var conf = ConfigFactory.load();
        var botToken = conf.getString("bot.token");

        DiscordClientBuilder.create(botToken).build()
                .gateway()
                .withGateway(gatewayClient -> Mono.when(
                                gatewayClient
                                        .on(MessageCreateEvent.class, event -> {
                                            if (event.getMessage().getAuthor().isEmpty()) {
                                                return Mono.empty();
                                            }

                                            if (!event.getMessage().getContent().equals("test")) {
                                                return Mono.empty();
                                            }

                                            if (event.getMessage().getAuthor().map(User::isBot).orElse(false)) {
                                                return Mono.empty();
                                            }

                                            return event.getMessage().getChannel()
                                                    .flatMap(channel -> channel.createMessage(
                                                            MessageCreateSpec.builder()
                                                                    .content("Test select menus")
                                                                    .addComponent(selectMenu1())
                                                                    .build()
                                                    ));
                                        })
                                        .then(),
                                gatewayClient
                                        .on(SelectMenuInteractionEvent.class, event -> {
                                            if (!"test_select_menu".equals(event.getCustomId())) {
                                                return Mono.empty();
                                            }

                                            if (event.getValues().contains("A") || event.getValues().contains("B")) {
                                                return event.edit()
                                                        .withEphemeral(false)
                                                        .withComponents(button1());
                                            }

                                            if (event.getValues().contains("C") || event.getValues().contains("D")) {
                                                return event.edit()
                                                        .withEphemeral(false)
                                                        .withComponents(button2());
                                            }

                                            return Mono.empty();
                                        })
                                        .then(),
                                gatewayClient
                                        .on(ButtonInteractionEvent.class, event -> {
                                            if ("test_button_1".equals(event.getCustomId())) {
                                                return event.edit()
                                                        .withEphemeral(false)
                                                        .withComponents(selectMenu2());
                                            }

                                            if ("test_button_2".equals(event.getCustomId())) {
                                                return event.edit()
                                                        .withEphemeral(false)
                                                        .withComponents(selectMenu1());
                                            }

                                            return Mono.empty();
                                        })
                        )
                ).block();
    }

    private static LayoutComponent selectMenu1() {
        return ActionRow.of(
                SelectMenu.of(
                        "test_select_menu",
                        SelectMenu.Option.of("A", "A"),
                        SelectMenu.Option.of("B", "B")
                ).withPlaceholder("Test placeholder").withMaxValues(1).withMaxValues(1)
        );
    }


    private static LayoutComponent selectMenu2() {
        return ActionRow.of(
                SelectMenu.of(
                        "test_select_menu",
                        SelectMenu.Option.of("C", "C"),
                        SelectMenu.Option.of("D", "D")
                ).withPlaceholder("Test placeholder").withMaxValues(1).withMaxValues(1)
        );
    }

    private static LayoutComponent button1() {
        return ActionRow.of(
                Button.primary(
                        "test_button_1",
                        "OK"
                )
        );
    }

    private static LayoutComponent button2() {
        return ActionRow.of(
                Button.primary(
                        "test_button_2",
                        "OK"
                )
        );
    }
}
