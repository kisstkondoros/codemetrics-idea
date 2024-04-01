package com.github.kisstkondoros.codemetrics.core.parser;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import org.jetbrains.uast.UElement;

import java.util.Map;
import java.util.Optional;

public class HandlerRegistry {
    private static final SimpleHandlerFactory GENERIC_HANDLER_FACTORY = new SimpleHandlerFactory();
    private static final ComplexityHandler<? extends UElement> UNKNOWN_HANDLER = GENERIC_HANDLER_FACTORY.create((c) -> 0, c -> "unknown");
    private static final Map<Class<? extends UElement>, ComplexityHandler<? extends UElement>> classHandlers = Maps.newHashMap();
    private static final CacheLoader<Class<? extends UElement>, ComplexityHandler<? extends UElement>> handlerCache =
            CacheLoader.from((Class<? extends UElement> elementTypeClass) -> {
                if (elementTypeClass != null) {
                    Optional<? extends ComplexityHandler<? extends UElement>> complexityHandler = classHandlers.keySet()
                            .stream()
                            .filter(p -> p.isAssignableFrom(elementTypeClass)).min((a, b) -> a == b ? 0 : a.isAssignableFrom(b) ? 1 : -1)
                            .map(classHandlers::get);
                    if (complexityHandler.isPresent()) {
                        return complexityHandler.get();
                    }
                }
                return UNKNOWN_HANDLER;
            });
    private static final LoadingCache<Class<? extends UElement>, ComplexityHandler<? extends UElement>> cache = CacheBuilder.newBuilder().build(handlerCache);

    static {
        UElementHandlerRegistry.register();
    }

    @SuppressWarnings("unchecked")
    static <T extends UElement> ComplexityHandler<T> get(UElement element) {
        try {
            return (ComplexityHandler<T>) cache.get(element.getClass());
        } catch (Exception e) {
            return (ComplexityHandler<T>) UNKNOWN_HANDLER;
        }
    }

    static <T extends UElement> void register(Class<T> type, ComplexityHandler<T> handler) {
        classHandlers.put(type, handler);
    }
}
