package com.fly.cloud.uiclient.service;

import com.fly.cloud.uiclient.service.netty.client.NettyClient;
import com.fly.cloud.uiclient.service.netty.client.NettyClientProtoBufHandler;
import com.fly.cloud.uiclient.vo.RecommendVO;
import com.fly.common.util.StringUtil;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch5_table.BETableUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class RootPanel extends JFrame {

    public static class single {
        public static RootPanel instance = new RootPanel();
    }

    public static RootPanel getInstance() {
        return single.instance;
    }


    JPanel rootJp = new JPanel();

    JPanel body = new JPanel();

    JPanel menu = new JPanel();
    JPanel foot = new JPanel();
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    JPanel panel4 = new JPanel();

    public void init() {
        this.setTitle(UIConstant.MAIN_TITLE);
        this.setSize(UIConstant.MAIN_WIDTH, UIConstant.MAIN_HEIGHT);
        this.setMinimumSize(new Dimension(UIConstant.MAIN_WIDTH, UIConstant.MAIN_HEIGHT));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /**
         * 设置根容器的布局类型
         */
        rootJp.setLayout(new BorderLayout());

        /**
         * 设置菜单栏的布局
         */
        int btnW = UIConstant.MAIN_WIDTH / 6;
        menu.setLayout(new GridLayout(1, 4));
        menu.setBackground(Color.LIGHT_GRAY);
        menu.setSize(new Dimension(btnW, 20));
        tabbedPane.addTab(UIConstant.MAIN_MENU_0, panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab(UIConstant.MAIN_MENU_1, panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.addTab(UIConstant.MAIN_MENU_2, panel3);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_3);
        tabbedPane.addTab(UIConstant.MAIN_MENU_3, panel4);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_4);
        menu.add(tabbedPane);

        rootJp.add(menu, BorderLayout.NORTH);
        body.setPreferredSize(new Dimension(UIConstant.MAIN_WIDTH, 375));
        rootJp.add(body, BorderLayout.CENTER);
        rootJp.add(foot, BorderLayout.SOUTH);
        this.add(rootJp);
    }

    public RootPanel() {

        init();
    }

    public void reloadPanel1(List<RecommendVO> list) {
        panel1.removeAll();
        panel1.add(RecommendTable.creaJTable(list));
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        //网络请求 建立网络连接
        final Channel channel = NettyClient.getInstance().getChannelFuture().channel();
        getInstance();
        channel.writeAndFlush(NettyClientProtoBufHandler.buildMethod("today"));
    }
}
