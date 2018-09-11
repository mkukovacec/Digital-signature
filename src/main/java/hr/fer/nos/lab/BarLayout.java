package hr.fer.nos.lab;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * <p>Created: 2018-05-18 3:55:28 PM</p>
 *
 * @author marin
 */
public class BarLayout extends HorizontalLayout {

    private static final long serialVersionUID = -958425225532725136L;

    public BarLayout() {
        setSpacing(true);
    }

    public BarLayout(final Component... p_components) {
        this();
        addComponents(p_components);
    }

    private Component wrapComponent(final Component p_component) {
        final FormLayout formLayout = new FormLayout();
        formLayout.addComponent(p_component);
        formLayout.setMargin(false);
        return formLayout;
    }

    private FormLayout findWrappedComponent(final Component p_component) {
        for (final Component component : components) {
            final FormLayout form = (FormLayout) component;
            if (form.getComponent(0).equals(p_component)) {
                return form;
            }
        }

        return null;
    }

    @Override
    public void addComponents(final Component... p_components) {
        for (final Component component : p_components) {
            addComponent(component);
        }
    }

    @Override
    public void addComponentAsFirst(final Component p_component) {
        super.addComponentAsFirst(wrapComponent(p_component));
    }

    @Override
    public void addComponent(final Component p_component) {
        super.addComponent(wrapComponent(p_component));
    }

    @Override
    public void addComponent(final Component p_component, final int p_index) {
        super.addComponent(wrapComponent(p_component), p_index);
    }

    @Override
    public void replaceComponent(final Component p_oldComponent, final Component p_newComponent) {
        final FormLayout form = findWrappedComponent(p_oldComponent);
        super.replaceComponent(form, wrapComponent(p_newComponent));
    }

    @Override
    public int getComponentIndex(final Component p_component) {
        final FormLayout form = findWrappedComponent(p_component);
        return super.getComponentIndex(form);
    }

    @Override
    public Component getComponent(final int p_index) throws IndexOutOfBoundsException {
        final FormLayout form = (FormLayout) super.getComponent(p_index);
        return form.getComponent(0);
    }

    @Override
    public void removeComponent(final Component p_component) {
        if (p_component == null) {
            return;
        }

        final FormLayout toRemove = findWrappedComponent(p_component);

        if (toRemove == null) {
            return;
        }

        toRemove.removeAllComponents();
        components.remove(toRemove);
        super.removeComponent(toRemove);
    }

}
