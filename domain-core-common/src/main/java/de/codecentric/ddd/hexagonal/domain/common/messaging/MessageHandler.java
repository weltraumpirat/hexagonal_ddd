package de.codecentric.ddd.hexagonal.domain.common.messaging;

import java.util.function.Consumer;

@FunctionalInterface
public interface MessageHandler<T extends Message<?>> extends Consumer<T> {
}
