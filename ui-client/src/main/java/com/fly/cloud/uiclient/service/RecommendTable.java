package com.fly.cloud.uiclient.service;

import com.fly.cloud.uiclient.vo.RecommendVO;
import org.jb2011.lnf.beautyeye.ch4_scroll.BEScrollPaneUI;
import org.jb2011.lnf.beautyeye.ch5_table.BETableUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.List;

public class RecommendTable {

    public static ComponentUI creaJTable(List<RecommendVO> list) {

        String[][] data;
            if (list == null || list.isEmpty()) {
                final int si = list.size();
                data = new String[si][];
                for (int i = 0; i < si; i++) {
                    RecommendVO vo = list.get(i);
                    String[] v = new String[]{vo.getCode(),vo.getName(),vo.getDate().toString(),vo.getReason()};
                    data[i] = v;
                }
            }else{
                data = new String [][]{};
            }
        final JTable table = new JTable(data, UIConstant.MAIN_MENU_0_HEAD);

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(UIConstant.MAIN_WIDTH, UIConstant.MAIN_HEIGHT);
        return BETableUI.createUI(table);
    }
}
