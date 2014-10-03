package de.hahn.blog.paneltabnavigate.view.beans;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.logging.ADFLogger;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.context.AdfFacesContext;

public class NavigateTabBean {
    private static ADFLogger _logger = ADFLogger.createADFLogger(NavigateTabBean.class);
    private static final String PANELTAB = "pt1";

    public NavigateTabBean() {
    }

    /**
     * Eventhandler to navigate to the next tab in a af:panelTabbed
     * @param actionEvent event which called the listener
     */
    public void naviGateButtonAction(ActionEvent actionEvent) {
        UIComponent ui = getUIComponent(PANELTAB);
        if (ui == null) {
            _logger.info("PanelTab component not found!");
            return;
        }
        if (!(ui instanceof RichPanelTabbed)) {
            _logger.info("Component is not an af:panelTabbed");
            return;
        }

        RichPanelTabbed rpt = (RichPanelTabbed) ui;
        int childCount = rpt.getChildCount();
        List<UIComponent> children = rpt.getChildren();
        for (int ii = 0; ii < childCount; ii++) {
            UIComponent uiSDI = children.get(ii);
            if (uiSDI instanceof RichShowDetailItem) {
                RichShowDetailItem rsdi = (RichShowDetailItem) uiSDI;
                if (rsdi.isDisclosed()) {
                    //close current tab
                    rsdi.setDisclosed(false);
                    //calculate next tab to disclose as next_tab_index = (current_tab_index + 1) % number_of_tabs
                    int kk = ii + 1;
                    int jj = kk % childCount;
                    _logger.info("old disclosed tab: " + ii + " new disclodes tab: " + jj);
                    RichShowDetailItem newSDI = (RichShowDetailItem) children.get(jj);
                    //open new tab
                    newSDI.setDisclosed(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(rpt);
                    return;
                }
            }
        }
    }

    public void nextTab() {
        naviGateButtonAction(null);
    }

    // find a jsf component
    private UIComponent getUIComponent(String name) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        return facesCtx.getViewRoot().findComponent(name);
    }


}
