package com.bennyhuo.kotlin.valuedef;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class ValueDefBundle extends DynamicBundle {
  @NonNls
  public static final String BUNDLE = "messages.ValueType";
  private static final ValueDefBundle INSTANCE = new ValueDefBundle();

  private ValueDefBundle() {
    super(BUNDLE);
  }

  @NotNull
  public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
    return INSTANCE.getMessage(key, params);
  }

  public static @Nls String partialMessage(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                           int unassignedParams,
                                           Object @NotNull ... params) {
    return INSTANCE.getPartialMessage(key, unassignedParams, params);
  }
  
  @NotNull
  public static Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
    return INSTANCE.getLazyMessage(key, params);
  }
}