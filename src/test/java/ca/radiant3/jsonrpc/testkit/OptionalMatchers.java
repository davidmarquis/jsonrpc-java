package ca.radiant3.jsonrpc.testkit;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Optional;

public class OptionalMatchers {

    public static <T> Matcher<Optional<T>> isPresentAnd(Matcher<T> matching) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(Object item) {
                Optional value = (Optional) item;
                return value.isPresent() && matching.matches(value.get());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is present and ");
                description.appendDescriptionOf(matching);
            }
        };
    }

    public static Matcher<Optional<?>> empty() {
        return new TypeSafeDiagnosingMatcher<>() {
            @Override
            protected boolean matchesSafely(Optional<?> item, Description mismatchDescription) {
                return !item.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("empty");
            }
        };
    }
}
