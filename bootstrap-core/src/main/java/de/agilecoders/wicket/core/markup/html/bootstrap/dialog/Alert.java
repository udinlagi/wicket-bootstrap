package de.agilecoders.wicket.core.markup.html.bootstrap.dialog;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapResourcesBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.ICssClassNameProvider;
import de.agilecoders.wicket.core.util.Attributes;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;

/**
 * A bootstrap styled panel for success, warning, and error messages
 *
 * usage:
 * <pre>
 *     add(new Alert("id", Model.of("message text")));
 * </pre>
 *
 * @author miha
 */
public class Alert extends GenericPanel<String> {

    /**
     * The {@code Type} enum defines all possible alert types.
     */
    public enum Type implements ICssClassNameProvider {
        Danger, Success, Info, Warning;

        @Override
        public String cssClassName() {
            return "alert-" + name().toLowerCase();
        }

        public static Type from(String level) {
            if (level.equalsIgnoreCase("ERROR") || level.equalsIgnoreCase("FATAL")) {
                return Danger;
            } else if (level.equalsIgnoreCase("WARNING")) {
                return Warning;
            } else if (level.equalsIgnoreCase("SUCCESS")) {
                return Success;
            } else {
                return Info;
            }
        }

    }

    private final WebMarkupContainer closeButton;
    private final Component message;
    private final Label blockHeader;
    private final Label inlineHeader;
    private final IModel<Type> type;
    private final IModel<Boolean> dismissable;
    private Duration duration;
    private boolean useInlineHeader = true;

    /**
     * Constructor.
     *
     * @param id      the wicket component id.
     * @param message the alert message
     */
    public Alert(String id, IModel<String> message) {
        this(id, message, Model.of(""));
    }

    /**
     * Constructor.
     *
     * @param id      the wicket component id.
     * @param message the alert message
     * @param header  the title of the alert message
     */
    public Alert(String id, IModel<String> message, IModel<String> header) {
        super(id, message);

        type = Model.of(Type.Info);
        dismissable = Model.of(Boolean.FALSE);

        this.inlineHeader = new Label("inline", header);
        this.blockHeader = new Label("block", header);
        this.message = createMessage("message", getModel());
        this.closeButton = new WebMarkupContainer("close");

        add(this.inlineHeader, this.blockHeader, this.message, this.closeButton);

        BootstrapBaseBehavior.addTo(this);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        checkComponentTag(tag, "div");
        Attributes.addClass(tag, "alert");
        Attributes.addClass(tag, type.getObject());

        if (dismissable.getObject()) {
            Attributes.addClass(tag, "alert-dismissable");
        } else {
            Attributes.removeClass(tag, "alert-dismissable");
        }
    }

    /**
     * creates a new message component.
     *
     * @param markupId The component id
     * @param message  The message as {@link IModel}
     * @return new message component
     */
    protected Component createMessage(final String markupId, final IModel<String> message) {
        return new Label(markupId, message);
    }

    /**
     * Sets whether the close button is visible.
     *
     * @param visible True if the close button is visible.
     * @return This
     */
    public Alert setCloseButtonVisible(boolean visible) {
        this.dismissable.setObject(visible);
        this.closeButton.setVisible(visible);
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        if (duration != null && duration.seconds() > 0) {
            response.render(OnDomReadyHeaderItem.forScript("window.setTimeout(function(){ $('#" + getMarkupId() + "').alert('close');}," +
                                                           duration.getMilliseconds() + ");"));
        }
    }

    /**
     * Sets the message.
     *
     * @param message The message.
     * @return This instance for chaining.
     */
    public Alert withMessage(final IModel<String> message) {
        setDefaultModel(message);

        return this;
    }

    /**
     * Sets the header message.
     *
     * @param header The header message.
     * @return This instance for chaining.
     */
    public Alert withHeader(IModel<String> header) {
        this.blockHeader.setDefaultModel(header);
        this.inlineHeader.setDefaultModel(header);

        return this;
    }

    /**
     * Sets the alert type.
     *
     * @param type to use.
     * @return This.
     */
    public Alert type(Type type) {
        this.type.setObject(type);

        return this;
    }

    /**
     * hides the alert box after given duration.
     *
     * @param duration the duration to use for closing the alert box
     * @return this.
     */
    public Alert hideAfter(Duration duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Whether to use an inline or block header.
     *
     * @param useInlineHeader true to use inline header
     * @return This
     */
    public Alert useInlineHeader(boolean useInlineHeader) {
        this.useInlineHeader = useInlineHeader;
        return this;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        if (Strings.isEmpty(inlineHeader.getDefaultModelObjectAsString())) {
            this.inlineHeader.setVisible(false);
            this.blockHeader.setVisible(false);
        } else {
            this.inlineHeader.setVisible(useInlineHeader);
            this.blockHeader.setVisible(!useInlineHeader);
        }

        this.message.setDefaultModel(getDefaultModel());

        if (closeButton.isVisible()) {
            add(BootstrapResourcesBehavior.instance());
        }
    }
}
