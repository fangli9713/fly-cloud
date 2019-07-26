package com.fly.cloud.uiclient;

import com.fly.cloud.uiclient.service.RootPanel;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;

public class UIClientApplication {
    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();

            //改变InsetsUIResource参数的值即可实现
            UIManager.put("TabbedPane.tabAreaInsets"
                    , new javax.swing.plaf.InsetsUIResource(2, 10, 2, 2));
            UIManager.put("RootPane.setupButtonVisible", false);
            UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
            //自定义JToolBar ui的border
            Border bd = new org.jb2011.lnf.beautyeye.ch8_toolbar.BEToolBarUI.ToolBarBorder(UIManager.getColor("ToolBar.shadow"),//Floatable时触点的颜色
                    UIManager.getColor("ToolBar.highlight"),//Floatable时触点的阴影颜色
                    new Insets(0, 0, 8, 0));//border的默认insets
            UIManager.put("ToolBar.border", new BorderUIResource(bd));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RootPanel.getInstance();
    }
}
