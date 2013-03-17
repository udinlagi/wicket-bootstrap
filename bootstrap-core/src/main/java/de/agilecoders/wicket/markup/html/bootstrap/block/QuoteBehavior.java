package de.agilecoders.wicket.markup.html.bootstrap.block;

import de.agilecoders.wicket.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.util.Attributes;
import de.agilecoders.wicket.util.Components;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Block-level element for quoting content from another source.
 * <p/>
 * <pre>
 *     <blockquote wicket:id="componentId">
 *          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis.</p>
 *          <small>Someone famous</small>
 *     </blockquote>
 * </pre>
 *
 * @author miha
 */
public class QuoteBehavior extends Behavior {

    private final IModel<String> pullRight;

    /**
     * Construct.
     */
    public QuoteBehavior() {
        super();

        this.pullRight = Model.of("");
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        super.onComponentTag(component, tag);

        Components.assertTag(component, tag, "blockquote");
        Attributes.addClass(tag, pullRight.getObject());
    }

    /**
     * sets the floating of the quote to "right".
     *
     * @return this component's instance
     */
    public final QuoteBehavior pullRight() {
        pullRight.setObject("pull-right");

        return this;
    }

    /**
     * sets the floating of the quote to "left".
     *
     * @return this component's instance
     */
    public final QuoteBehavior pullLeft() {
        pullRight.setObject("");

        return this;
    }

    @Override
    public void bind(final Component component) {
        super.bind(component);

        BootstrapBaseBehavior.addTo(component);
    }

    @Override
    public void unbind(final Component component) {
        super.unbind(component);

        BootstrapBaseBehavior.removeFrom(component);
    }
}