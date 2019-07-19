package com.fly.cloud.uiclient.service;

import com.fly.cloud.uiclient.vo.RecommendVO;
import org.checkerframework.checker.guieffect.qual.UI;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecommendTable {

    public static Component creaJTable(List<RecommendVO> list) {

        String[][] data = null;
            if (!CollectionUtils.isEmpty(list)) {
                final int si = list.size();
                data = new String[si][];
                for (int i = 0; i < si; i++) {
                    RecommendVO vo = list.get(i);
                    String[] v = new String[]{vo.getCode(),vo.getName(),vo.getDate().toString(),vo.getReason()};
                    data[i] = v;
                }
            }
        final JTable table = new JTable(data, UIConstant.MAIN_MENU_0_HEAD);
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(UIConstant.MAIN_WIDTH, UIConstant.MAIN_HEIGHT);
        return scrollPane;
    }
}
