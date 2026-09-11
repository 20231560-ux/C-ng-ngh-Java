package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * TABLE INSPECTOR PANEL (PANEL THÔNG TIN BÀN CHUYÊN BIỆT)
 * Hiển thị thông tin chi tiết và hành động thời gian thực cho bàn đang được chọn
 */
public class TableInspectorPanel extends SavoreDesignSystem.ModernCard {

    private final JLabel lblTableName;
    private final JLabel lblAreaName;
    private final JLabel lblStatusBadge;
    private final JLabel lblSeats;
    private final JLabel lblTimeSeated;
    private final JLabel lblCustomer;
    private final JLabel lblBillAmount;
    private final JLabel lblNote;

    private ModernFloorPlanMap.TableNode currentNode;
    private final Consumer<String> chuyenPhanHeCallback;

    public TableInspectorPanel(Consumer<String> chuyenPhanHeCallback) {
        super(16, Color.WHITE);
        this.chuyenPhanHeCallback = chuyenPhanHeCallback;
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(0, 0, 16, 0));

        // 1. BANNER TRÊN ĐỈNH MINH HỌA KHU VỰC
        JPanel pnlBanner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0F172A), getWidth(), getHeight(), new Color(0x1E293B));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 16, 16);

                // Điểm nhấn vàng
                g2.setColor(new Color(0xB0, 0x82, 0x46, 70));
                g2.fillOval(getWidth() - 60, -20, 100, 100);
                g2.dispose();
            }
        };
        pnlBanner.setPreferredSize(new Dimension(100, 72));
        pnlBanner.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel pnlBannerText = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlBannerText.setOpaque(false);

        lblTableName = new JLabel("BÀN 03");
        lblTableName.setFont(SavoreDesignSystem.Fonts.get(16, Font.BOLD));
        lblTableName.setForeground(Color.WHITE);

        lblAreaName = new JLabel("TẦNG 1 — SẢNH CHÍNH");
        lblAreaName.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        lblAreaName.setForeground(SavoreDesignSystem.Colors.GOLD_LIGHT);

        pnlBannerText.add(lblTableName);
        pnlBannerText.add(lblAreaName);
        pnlBanner.add(pnlBannerText, BorderLayout.WEST);

        lblStatusBadge = new JLabel("ĐANG PHỤC VỤ", SwingConstants.CENTER);
        lblStatusBadge.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        lblStatusBadge.setForeground(Color.WHITE);
        lblStatusBadge.setOpaque(true);
        lblStatusBadge.setBackground(new Color(0x2563EB));
        lblStatusBadge.setBorder(new EmptyBorder(4, 10, 4, 10));
        JPanel pnlBadgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        pnlBadgeWrap.setOpaque(false);
        pnlBadgeWrap.add(lblStatusBadge);
        pnlBanner.add(pnlBadgeWrap, BorderLayout.EAST);

        add(pnlBanner, BorderLayout.NORTH);

        // 2. THÔNG TIN CHI TIẾT
        JPanel pnlDetails = new JPanel();
        pnlDetails.setOpaque(false);
        pnlDetails.setLayout(new BoxLayout(pnlDetails, BoxLayout.Y_AXIS));
        pnlDetails.setBorder(new EmptyBorder(0, 16, 0, 16));

        lblSeats = taoDongThongTin("🪑 Sức chứa:", "4 - 6 Chỗ ngồi (Bàn chữ nhật)");
        lblTimeSeated = taoDongThongTin("⏱️ Thời gian ngồi:", "45 phút (từ 11:45)");
        lblCustomer = taoDongThongTin("👤 Khách hàng:", "Anh Hoàng • VIP Gold");
        lblBillAmount = taoDongThongTin("💰 Tạm tính:", "1.850.000 đ");
        lblBillAmount.setFont(SavoreDesignSystem.Fonts.get(13, Font.BOLD));
        lblBillAmount.setForeground(SavoreDesignSystem.Colors.GOLD_DARK);

        lblNote = taoDongThongTin("📝 Ghi chú:", "Khách kỷ niệm ngày cưới, chuẩn bị nến");

        pnlDetails.add(lblSeats.getParent());
        pnlDetails.add(lblTimeSeated.getParent());
        pnlDetails.add(lblCustomer.getParent());
        pnlDetails.add(lblBillAmount.getParent());
        pnlDetails.add(lblNote.getParent());

        add(pnlDetails, BorderLayout.CENTER);

        // 3. CÁC NÚT HÀNH ĐỘNG NGHIỆP VỤ (2x2 GRID)
        JPanel pnlActions = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlActions.setOpaque(false);
        pnlActions.setBorder(new EmptyBorder(8, 16, 0, 16));

        SavoreDesignSystem.ModernButton btnView = new SavoreDesignSystem.ModernButton("🍽️ Gọi Món POS", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnView.addActionListener(e -> {
            if (chuyenPhanHeCallback != null) chuyenPhanHeCallback.accept("POS");
        });

        SavoreDesignSystem.ModernButton btnPay = new SavoreDesignSystem.ModernButton("💳 Thanh Toán", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnPay.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Mở cổng thanh toán cho " + lblTableName.getText() + "\nTổng tiền: " + lblBillAmount.getText(), "Thanh toán hóa đơn", JOptionPane.INFORMATION_MESSAGE);
        });

        SavoreDesignSystem.ModernButton btnMerge = new SavoreDesignSystem.ModernButton("🔗 Gộp Bàn", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnMerge.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chọn bàn để gộp với " + lblTableName.getText(), "Gộp bàn phục vụ", JOptionPane.INFORMATION_MESSAGE);
        });

        SavoreDesignSystem.ModernButton btnMove = new SavoreDesignSystem.ModernButton("🔄 Chuyển Bàn", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnMove.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chọn vị trí mới để chuyển khách từ " + lblTableName.getText(), "Chuyển bàn", JOptionPane.INFORMATION_MESSAGE);
        });

        pnlActions.add(btnView);
        pnlActions.add(btnPay);
        pnlActions.add(btnMerge);
        pnlActions.add(btnMove);

        add(pnlActions, BorderLayout.SOUTH);
    }

    private JLabel taoDongThongTin(String label, String initialVal) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0xF1F5F9)));
        row.setPreferredSize(new Dimension(100, 32));

        JLabel lblL = new JLabel(label);
        lblL.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblL.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        JLabel lblV = new JLabel(initialVal, SwingConstants.RIGHT);
        lblV.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblV.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);

        row.add(lblL, BorderLayout.WEST);
        row.add(lblV, BorderLayout.CENTER);
        return lblV;
    }

    public void capNhatThongTin(ModernFloorPlanMap.TableNode node) {
        this.currentNode = node;
        if (node == null) return;

        lblTableName.setText(node.name.toUpperCase());
        lblAreaName.setText(node.area.toUpperCase());
        lblSeats.setText(node.seats + " Chỗ ngồi (" + getShapeName(node.shape) + ")");

        switch (node.status) {
            case TRONG:
                lblStatusBadge.setText("TRỐNG");
                lblStatusBadge.setBackground(SavoreDesignSystem.Colors.STATUS_SUCCESS);
                lblTimeSeated.setText("Chưa có khách");
                lblCustomer.setText("—");
                lblBillAmount.setText("0 đ");
                lblNote.setText(node.note.isEmpty() ? "Sẵn sàng đón khách mới" : node.note);
                break;
            case DANG_DUNG:
                lblStatusBadge.setText("ĐANG PHỤC VỤ");
                lblStatusBadge.setBackground(new Color(0x2563EB));
                lblTimeSeated.setText(node.timeSeated.isEmpty() ? "Đã ngồi 30 phút" : node.timeSeated);
                lblCustomer.setText(node.customerName.isEmpty() ? "Khách vãng lai" : node.customerName);
                DecimalFormat df = new DecimalFormat("#,### đ");
                lblBillAmount.setText(node.currentBill > 0 ? df.format(node.currentBill) : "Chưa gọi món");
                lblNote.setText(node.note.isEmpty() ? "Đang phục vụ bữa ăn" : node.note);
                break;
            case DAT_TRUOC:
                lblStatusBadge.setText("ĐÃ ĐẶT TRƯỚC");
                lblStatusBadge.setBackground(SavoreDesignSystem.Colors.STATUS_WARNING);
                lblTimeSeated.setText("Lịch hẹn: " + (node.timeSeated.isEmpty() ? "Hôm nay" : node.timeSeated));
                lblCustomer.setText(node.customerName.isEmpty() ? "Khách đặt trước" : node.customerName);
                lblBillAmount.setText("Đã đặt cọc");
                lblNote.setText(node.note.isEmpty() ? "Bàn đã giữ chỗ, sẵn sàng đón tiếp" : node.note);
                break;
            case CAN_DON:
                lblStatusBadge.setText("CẦN DỌN DẸP");
                lblStatusBadge.setBackground(SavoreDesignSystem.Colors.STATUS_PURPLE);
                lblTimeSeated.setText("Vừa trả bàn");
                lblCustomer.setText("—");
                lblBillAmount.setText("Đã thanh toán");
                lblNote.setText("Cần nhân viên dọn bàn ngay");
                break;
            default:
                lblStatusBadge.setText("TẠM KHÓA");
                lblStatusBadge.setBackground(new Color(0x94A3B8));
                lblTimeSeated.setText("—");
                lblCustomer.setText("—");
                lblBillAmount.setText("—");
                lblNote.setText("Bàn bảo trì hoặc dành riêng sự kiện");
                break;
        }
        revalidate();
        repaint();
    }

    private String getShapeName(ModernFloorPlanMap.TableShape shape) {
        switch (shape) {
            case SQUARE: return "Bàn vuông";
            case RECTANGLE: return "Bàn chữ nhật";
            case ROUND: return "Bàn tròn";
            case VIP: return "Bàn VIP Hoàng Gia";
            default: return "Bàn tiêu chuẩn";
        }
    }
}
