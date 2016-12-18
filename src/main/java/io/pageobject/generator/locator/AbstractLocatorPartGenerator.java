package io.pageobject.generator.locator;

import io.pageobject.generator.GeneratorContext;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static io.pageobject.generator.Expressions.replaceIndexBindingWithFunctionParameter;
import static io.pageobject.generator.locator.LocatorPart.*;
import static java.util.stream.Collectors.toList;

public abstract class AbstractLocatorPartGenerator implements LocatorPartGenerator {

    public static final String ELEMENT_FINDER = "element";
    public static final String ELEMENT_ARRAY_FINDER = "element.all";
    public static final String ELEMENT_ARRAY_FINDER_IN_REPEATER = "all";

    private final LocatorSources[] locatorCandidates;
    protected final Element element;

    public AbstractLocatorPartGenerator(Element element, LocatorSources... locatorCandidates) {
        checkArgument(locatorCandidates.length > 0, "There must be at least one locator candidate");
        this.element = element;
        this.locatorCandidates = locatorCandidates;
    }

    @Override
    public LocatorPart firstPart(GeneratorContext context) {
        return invalidPart();
    }

    @Override
    public LocatorPart middlePart(GeneratorContext context, int index) {
        return invalidPart();
    }

    @Override
    public LocatorPart lastPartForRepeaterElement(GeneratorContext context, int index) {
        return emptyPart();
    }

    protected LocatorPart generateLastPart(GeneratorContext context, String elementFinder, int depth) {
        Element element = context.getElement();

        List<LocatorSources> sources = Arrays.stream(locatorCandidates)
                                             .filter(locatorSource -> locatorSource.isEnabledForApplicationType(context.getApplicationType()))
                                             .collect(toList());

        for (LocatorSources locatorCandidate : sources) {
            String attributeValue = locatorCandidate.extractLocatorValue(element, context);

            if (!isNullOrEmpty(attributeValue) && context.isLocatorAvailable(locatorCandidate, attributeValue)) {
                StringBuilder value = new StringBuilder(elementFinder);
                value.append("(");
                value.append((locatorCandidate.locator(replaceIndexBindingWithFunctionParameter(attributeValue,
                                                                                                depth))));
                value.append(")");

                return part(value.toString(), locatorCandidate);
            }
        }

        return invalidPart();
    }

    @Override
    public Element getElement() {
        return element;
    }
}
