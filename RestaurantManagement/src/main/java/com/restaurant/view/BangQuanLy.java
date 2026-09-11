package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

public abstract class BangQuanLy extends JPanel {

    public static final Color NEN     = Giao.NEN;
    public static final Color KHOI    = Giao.THE;
    public static final Color KHOI_2  = Giao.THE;
    public static final Color HANG    = new Color(0xFAFBFB);
    public static final Color VIEN    = Giao.VIEN;
    public static final Color NGOC    = Giao.CHINH;
    public static final Color NGOC_MO = Giao.CHINH_DAM;
    public static final Color CHU     = Giao.CHU;
    public static final Color CHU_MO  = Giao.CHU_PHU;
    public static final Color LUC     = Giao.LUC;
    public static final Color CAM     = Giao.CAM;
    public static final Color DO      = Giao.DO;
    public static final Color LAM     = Giao.LAM;
    public static final Color TIM     = Giao.TIM;
    public static final String FONT   = Giao.FONT;

    protected final List<Object[]> duLieu = new ArrayList<>();
    protected final List<Object> khoaHang = new ArrayList<>();
    protected Cot[] cot;
    protected boolean dungCSDL = false;
    protected String loiCSDL;

    private final Set<String> cotCoThat = new HashSet<>();
    protected DefaultTableModel model;
    protected JTable bang;
    protected TableRowSorter<DefaultTableModel> locBang;
    protected final JPanel oThongKe = new JPanel(new GridLayout(1, 4, 16, 0));
    protected Giao.O tim;
    protected JLabel lbDem, lbNguon;
    private int hangHover = -1;
    private int cotHanhDong = -1;
    private JPanel vungBang;
    private final CardLayout theBang = new CardLayout();

    protected abstract String tenThucThe();
    protected abstract Cot[] dinhNghiaCot();
    protected abstract void napMau(List<Object[]> ds);
    protected abstract JPanel[] thongKe();

    protected String bangDB() { return null; }
    protected String khoaDB() { return null; }
    protected String sapXepDB() { return null; }
    protected void tinhThem(Object[] hang) { }

    protected Color mauNhan(String giaTri) {
        String s = giaTri == null ? "" : giaTri.toLowerCase();
        if (s.contains("ngừng") || s.contains("hết") || s.contains("khoá") || s.contains("khóa")
                || s.contains("nghỉ") || s.contains("huỷ")) return DO;
        if (s.contains("sắp") || s.contains("chờ") || s.contains("tạm")) return CAM;
        if (s.contains("đang") || s.contains("hoạt") || s.contains("đủ") || s.contains("còn")
                || s.contains("hiển thị")) return LUC;
        return LAM;
    }

    /* ==================== DỰNG ==================== */
    protected void dungGiaoDien() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 18));
        cot = dinhNghiaCot();
        napTuCSDL();
        if (!dungCSDL) {
            duLieu.clear();
            khoaHang.clear();
            napMau(duLieu);
            for (Object[] r : duLieu) { tinhThem(r); khoaHang.add(null); }
        }

        oThongKe.setOpaque(false);
        oThongKe.setPreferredSize(new Dimension(100, 112));
        add(oThongKe, BorderLayout.NORTH);
        add(khungChinh(), BorderLayout.CENTER);
        veLai();
    }

    private JPanel khungChinh() {
        Giao.The k = new Giao.The(new BorderLayout(0, 0));
        k.setBorder(new EmptyBorder(0, 0, 0, 0));
        k.add(thanhCongCu(), BorderLayout.NORTH);
        k.add(vungDuLieu(), BorderLayout.CENTER);
        k.add(chanTrang(), BorderLayout.SOUTH);
        return k;
    }

    private JPanel thanhCongCu() {
        JPanel p = new JPanel(new BorderLayout(14, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel trai = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        trai.setOpaque(false);
        tim = new Giao.O("Tìm trong " + tenThucThe() + "…", true);
        tim.setPreferredSize(new Dimension(300, 38));
        tim.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { loc(); }
        });
        lbDem = Giao.chu("", 12, Font.PLAIN, Giao.CHU_PHU);
        lbNguon = Giao.chu("", 11, Font.BOLD, Giao.CHU_PHU);
        trai.add(tim);
        trai.add(lbDem);
        p.add(trai, BorderLayout.WEST);

        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        phai.setOpaque(false);
        Giao.Nut tai = new Giao.Nut("Tải lại", Giao.Nut.PHU).icon("taive");
        Giao.Nut xuat = new Giao.Nut("Xuất CSV", Giao.Nut.PHU).icon("xuat");
        Giao.Nut them = new Giao.Nut("Thêm " + tenThucThe(), Giao.Nut.CHINH_NUT).icon("them");
        tai.addActionListener(e -> { taiLai(); Giao.bao(this, "Đã tải lại dữ liệu", Giao.LUC); });
        xuat.addActionListener(e -> xuatCsv());
        them.addActionListener(e -> moForm(-1));
        phai.add(lbNguon);
        phai.add(tai);
        phai.add(xuat);
        phai.add(them);
        p.add(phai, BorderLayout.EAST);
        return p;
    }

    private JPanel chanTrang() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xFAFBFB));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Giao.VIEN);
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 44));
        p.setBorder(new EmptyBorder(0, 18, 0, 18));
        JLabel meo = Giao.chu("Nháy đúp vào một dòng để chỉnh sửa  ·  Rê chuột vào dòng để hiện nút thao tác",
                11, Font.PLAIN, Giao.CHU_NHAT);
        p.add(meo, BorderLayout.WEST);
        return p;
    }

    private JPanel vungDuLieu() {
        String[] ten = new String[cot.length + 1];
        for (int i = 0; i < cot.length; i++) ten[i] = cot[i].ten;
        ten[cot.length] = "";
        cotHanhDong = cot.length;

        model = new DefaultTableModel(ten, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bang = new JTable(model);
        bang.setRowHeight(58);
        bang.setShowGrid(false);
        bang.setIntercellSpacing(new Dimension(0, 0));
        bang.setBackground(Color.WHITE);
        bang.setSelectionBackground(Giao.CHINH_NHAT);
        bang.setSelectionForeground(Giao.CHU);
        bang.setFillsViewportHeight(true);
        bang.setFont(Giao.f(13, Font.PLAIN));
        bang.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader h = bang.getTableHeader();
        h.setPreferredSize(new Dimension(10, 46));
        h.setReorderingAllowed(false);
        h.setDefaultRenderer((t, v, s, f, r, c) -> new JLabel(String.valueOf(v)) {
            {
                setOpaque(false);
                setFont(Giao.f(11, Font.BOLD));
                setForeground(Giao.CHU_PHU);
                setBorder(new EmptyBorder(0, 16, 0, 12));
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xFAFBFB));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Giao.VIEN);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
                super.paintComponent(g);
            }
        });

        bang.setDefaultRenderer(Object.class, new OBang());
        locBang = new TableRowSorter<>(model);
        bang.setRowSorter(locBang);

        bang.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                int r = bang.rowAtPoint(e.getPoint());
                if (r != hangHover) { hangHover = r; bang.repaint(); }
            }
        });
        bang.addMouseListener(new MouseAdapter() {
            @Override public void mouseExited(MouseEvent e) { hangHover = -1; bang.repaint(); }
            @Override public void mouseClicked(MouseEvent e) {
                int r = bang.rowAtPoint(e.getPoint());
                int c = bang.columnAtPoint(e.getPoint());
                if (r < 0) return;
                if (c == cotHanhDong) {
                    Rectangle o = bang.getCellRect(r, c, true);
                    int x = e.getX() - o.x;
                    bang.setRowSelectionInterval(r, r);
                    if (x < o.width / 2) moForm(bang.convertRowIndexToModel(r));
                    else xoa();
                    return;
                }
                if (e.getClickCount() == 2) moForm(bang.convertRowIndexToModel(r));
            }
        });

        JScrollPane sp = new JScrollPane(bang);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setBackground(Color.WHITE);
        sp.getVerticalScrollBar().setUnitIncrement(22);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        sp.getVerticalScrollBar().setUI(new Giao.Cuon());

        vungBang = new JPanel(theBang);
        vungBang.setOpaque(false);
        vungBang.add(sp, "bang");
        vungBang.add(new TrangRong(), "rong");
        return vungBang;
    }

    /* ==================== Ô BẢNG ==================== */
    private class OBang extends JComponent implements TableCellRenderer {
        String vb = "";
        int kieu = 0, cotIdx = 0, hangIdx = 0;
        boolean chon, hover, hanhDong;
        Color mau = Giao.CHU;

        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s,
                                                                 boolean f, int r, int c) {
            cotIdx = t.convertColumnIndexToModel(c);
            hanhDong = cotIdx == cotHanhDong;
            vb = v == null ? "" : v.toString();
            kieu = hanhDong ? -1 : cot[cotIdx].kieu;
            chon = s;
            hover = r == hangHover;
            hangIdx = r;
            mau = kieu == Cot.NHAN ? mauNhan(vb) : (kieu == Cot.TIEN ? Giao.CHU : Giao.CHU_PHU);
            return this;
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            g2.setColor(chon ? Giao.CHINH_NHAT : hover ? new Color(0xF6F6F7) : Color.WHITE);
            g2.fillRect(0, 0, w, h);
            g2.setColor(new Color(0xF1F2F3));
            g2.fillRect(0, h - 1, w, 1);
            if (chon && cotIdx == 0) {
                g2.setColor(Giao.CHINH);
                g2.fillRect(0, 0, 3, h);
            }

            if (hanhDong) {
                if (hover || chon) {
                    veNutNho(g2, 8, h / 2 - 15, "sua", Giao.LAM);
                    veNutNho(g2, 48, h / 2 - 15, "xoa", Giao.DO);
                }
                g2.dispose();
                return;
            }

            if (kieu == Cot.NHAN && !vb.isEmpty()) {
                Giao.veHuyHieu(g2, vb, 16, h / 2 - 11, mau);
            } else if (kieu == Cot.DAM) {
                int x = 16;
                if (cotIdx <= 1) {
                    Color m = mauChu(vb);
                    g2.setColor(Giao.pha(m, Color.WHITE, 0.82f));
                    g2.fillRoundRect(14, h / 2 - 17, 34, 34, 11, 11);
                    g2.setColor(m.darker());
                    g2.setFont(Giao.f(13, Font.BOLD));
                    String vt = vb.isEmpty() ? "?" : vb.substring(0, 1).toUpperCase();
                    g2.drawString(vt, 31 - g2.getFontMetrics().stringWidth(vt) / 2, h / 2 + 5);
                    x = 58;
                }
                g2.setFont(Giao.f(13, Font.BOLD));
                g2.setColor(Giao.CHU);
                g2.drawString(catChu(g2, vb, w - x - 14), x, h / 2 + 5);
            } else {
                boolean phai = kieu == Cot.TIEN || kieu == Cot.SO;
                g2.setFont(Giao.f(13, kieu == Cot.TIEN ? Font.BOLD : Font.PLAIN));
                g2.setColor(mau);
                String s = catChu(g2, vb, w - 32);
                g2.drawString(s, phai ? w - 16 - g2.getFontMetrics().stringWidth(s) : 16, h / 2 + 5);
            }
            g2.dispose();
        }

        private void veNutNho(Graphics2D g2, int x, int y, String icon, Color m) {
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y, 32, 30, 8, 8);
            g2.setColor(Giao.VIEN_DAM);
            g2.drawRoundRect(x, y, 32, 30, 8, 8);
            Giao.Ic.ve(g2, icon, x + 8, y + 7, 16, m);
        }

        private String catChu(Graphics2D g2, String s, int rong) {
            FontMetrics fm = g2.getFontMetrics();
            if (fm.stringWidth(s) <= rong) return s;
            while (fm.stringWidth(s + "…") > rong && s.length() > 1) s = s.substring(0, s.length() - 1);
            return s + "…";
        }

        private Color mauChu(String s) {
            Color[] p = {Giao.CHINH, Giao.LAM, Giao.TIM, Giao.CAM, new Color(0x0D9488), new Color(0xBE185D)};
            return p[Math.abs(s.hashCode()) % p.length];
        }
    }

    /* ==================== TRẠNG THÁI RỖNG ==================== */
    private class TrangRong extends JPanel {
        TrangRong() {
            setOpaque(false);
            setBackground(Color.WHITE);
            setLayout(new GridBagLayout());
            JPanel giua = new JPanel();
            giua.setOpaque(false);
            giua.setLayout(new BoxLayout(giua, BoxLayout.Y_AXIS));
            JComponent hinh = new JComponent() {
                @Override public Dimension getPreferredSize() { return new Dimension(96, 96); }
                @Override public Dimension getMaximumSize() { return new Dimension(96, 96); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Giao.CHINH_NHAT);
                    g2.fillOval(0, 0, 96, 96);
                    g2.setColor(Giao.CHINH);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawRoundRect(30, 28, 36, 40, 6, 6);
                    g2.drawLine(30, 40, 66, 40);
                    g2.drawLine(38, 50, 58, 50);
                    g2.drawLine(38, 58, 52, 58);
                    g2.dispose();
                }
            };
            hinh.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel l1 = Giao.chu("Chưa có " + tenThucThe() + " nào", 17, Font.BOLD, Giao.CHU);
            JLabel l2 = Giao.chu("Thêm bản ghi đầu tiên để bắt đầu quản lý", 12, Font.PLAIN, Giao.CHU_PHU);
            l1.setAlignmentX(Component.CENTER_ALIGNMENT);
            l2.setAlignmentX(Component.CENTER_ALIGNMENT);
            Giao.Nut n = new Giao.Nut("Thêm " + tenThucThe(), Giao.Nut.CHINH_NUT);
            n.icon("them");
            n.setAlignmentX(Component.CENTER_ALIGNMENT);
            n.addActionListener(e -> moForm(-1));
            giua.add(hinh);
            giua.add(Box.createRigidArea(new Dimension(0, 18)));
            giua.add(l1);
            giua.add(Box.createRigidArea(new Dimension(0, 8)));
            giua.add(l2);
            giua.add(Box.createRigidArea(new Dimension(0, 20)));
            giua.add(n);
            add(giua);
        }
        @Override protected void paintComponent(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /* ==================== CSDL ==================== */
    private void napTuCSDL() {
        if (bangDB() == null) return;
        try (Connection c = DatabaseConnection.getConnection()) {
            if (c == null) return;
            docCot(c);
            if (cotCoThat.isEmpty()) return;
            List<Cot> giu = new ArrayList<>();
            for (Cot ct : cot)
                if (ct.db == null || cotCoThat.contains(ct.db.toLowerCase())) giu.add(ct);
            cot = giu.toArray(new Cot[0]);
            for (Cot ct : cot) if (ct.fkBang != null) napFk(c, ct);
            docDuLieu(c);
            dungCSDL = true;
        } catch (Throwable e) {
            loiCSDL = e.getMessage();
            dungCSDL = false;
        }
    }

    private void docCot(Connection c) throws SQLException {
        cotCoThat.clear();
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + bangDB() + " LIMIT 0")) {
            ResultSetMetaData m = rs.getMetaData();
            for (int i = 1; i <= m.getColumnCount(); i++) cotCoThat.add(m.getColumnLabel(i).toLowerCase());
        }
    }

    private void napFk(Connection c, Cot ct) {
        ct.idTen = new LinkedHashMap<>();
        ct.tenId = new LinkedHashMap<>();
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT " + ct.fkKhoa + "," + ct.fkTen + " FROM " + ct.fkBang)) {
            while (rs.next()) {
                String id = rs.getString(1), t = rs.getString(2);
                if (t == null) t = id;
                ct.idTen.put(id, t);
                ct.tenId.put(t, id);
            }
        } catch (Exception ignore) { }
        ct.chon = ct.tenId.keySet().toArray(new String[0]);
    }

    private void docDuLieu(Connection c) throws SQLException {
        duLieu.clear();
        khoaHang.clear();
        StringBuilder q = new StringBuilder("SELECT * FROM ").append(bangDB());
        if (sapXepDB() != null) q.append(" ORDER BY ").append(sapXepDB());
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(q.toString())) {
            while (rs.next()) {
                Object[] r = new Object[cot.length];
                for (int i = 0; i < cot.length; i++) r[i] = hienThi(rs, cot[i]);
                tinhThem(r);
                duLieu.add(r);
                khoaHang.add(khoaDB() == null ? null : rs.getObject(khoaDB()));
            }
        }
    }

    private String hienThi(ResultSet rs, Cot ct) {
        if (ct.db == null) return "";
        try {
            Object v = rs.getObject(ct.db);
            if (v == null) return "";
            String s = String.valueOf(v).trim();
            if (ct.fkBang != null && ct.idTen != null) return ct.idTen.getOrDefault(s, s);
            if (ct.batTat != null) {
                boolean b = s.equals("1") || s.equalsIgnoreCase("true");
                return b ? ct.batTat[0] : ct.batTat[1];
            }
            if (ct.kieu == Cot.TIEN) return Giao.tien(Double.parseDouble(s));
            if (ct.kieu == Cot.SO) {
                double d = Double.parseDouble(s);
                return d == Math.floor(d) ? String.valueOf((long) d) : Giao.so(d);
            }
            if (s.length() > 10 && s.charAt(4) == '-' && s.charAt(7) == '-')
                return s.substring(8, 10) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
            return s;
        } catch (Exception e) {
            return "";
        }
    }

    private Object giaTriLuu(Cot ct, String s) {
        if (ct.fkBang != null && ct.tenId != null) return ct.tenId.get(s);
        if (ct.batTat != null) return s.equals(ct.batTat[0]) ? 1 : 0;
        if (ct.kieu == Cot.TIEN || ct.kieu == Cot.SO) return soTu(s);
        if (s.length() == 10 && s.charAt(2) == '/' && s.charAt(5) == '/')
            return s.substring(6) + "-" + s.substring(3, 5) + "-" + s.substring(0, 2);
        return s;
    }

    private boolean luuCSDL(Object[] hang, Object khoa) {
        if (!dungCSDL) return true;
        List<String> ten = new ArrayList<>();
        List<Object> gt = new ArrayList<>();
        for (int i = 0; i < cot.length; i++) {
            Cot ct = cot[i];
            if (ct.db == null || !ct.suaDuoc) continue;
            ten.add(ct.db);
            gt.add(giaTriLuu(ct, String.valueOf(hang[i])));
        }
        if (ten.isEmpty()) return false;
        StringBuilder q = new StringBuilder();
        if (khoa == null) {
            q.append("INSERT INTO ").append(bangDB()).append(" (");
            for (int i = 0; i < ten.size(); i++) q.append(i > 0 ? "," : "").append(ten.get(i));
            q.append(") VALUES (");
            for (int i = 0; i < ten.size(); i++) q.append(i > 0 ? ",?" : "?");
            q.append(")");
        } else {
            q.append("UPDATE ").append(bangDB()).append(" SET ");
            for (int i = 0; i < ten.size(); i++) q.append(i > 0 ? "," : "").append(ten.get(i)).append("=?");
            q.append(" WHERE ").append(khoaDB()).append("=?");
            gt.add(khoa);
        }
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(q.toString())) {
            for (int i = 0; i < gt.size(); i++) ps.setObject(i + 1, gt.get(i));
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            Giao.bao(this, "Không lưu được: " + e.getMessage(), Giao.DO);
            return false;
        }
    }

    private boolean xoaCSDL(Object khoa) {
        if (!dungCSDL || khoa == null) return true;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM " + bangDB() + " WHERE " + khoaDB() + "=?")) {
            ps.setObject(1, khoa);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            Giao.bao(this, "Không xoá được: " + e.getMessage(), Giao.DO);
            return false;
        }
    }

    protected void taiLai() {
        if (!dungCSDL) { veLai(); return; }
        try (Connection c = DatabaseConnection.getConnection()) {
            docDuLieu(c);
        } catch (Exception ignore) { }
        veLai();
    }

    /* ==================== VẼ LẠI ==================== */
    protected void veLai() {
        model.setRowCount(0);
        for (Object[] r : duLieu) {
            Object[] d = new Object[cot.length + 1];
            System.arraycopy(r, 0, d, 0, cot.length);
            d[cot.length] = "";
            model.addRow(d);
        }
        for (int i = 0; i < cot.length && i < bang.getColumnCount(); i++)
            bang.getColumnModel().getColumn(i).setPreferredWidth(cot[i].rong);
        if (bang.getColumnCount() > cot.length) {
            TableColumn tc = bang.getColumnModel().getColumn(cot.length);
            tc.setPreferredWidth(92);
            tc.setMaxWidth(92);
            tc.setMinWidth(92);
        }
        JPanel[] tk = thongKe();
        oThongKe.removeAll();
        for (JPanel p : tk) oThongKe.add(p);
        oThongKe.revalidate();
        oThongKe.repaint();
        if (lbNguon != null) {
            lbNguon.setText(dungCSDL ? "●  MySQL · " + bangDB() : "●  Dữ liệu mẫu");
            lbNguon.setForeground(dungCSDL ? Giao.LUC : Giao.CAM);
        }
        loc();
    }

    private void loc() {
        String q = tim == null ? "" : tim.getText().trim();
        locBang.setRowFilter(q.isEmpty() ? null
                : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(q)));
        if (lbDem != null)
            lbDem.setText("   " + bang.getRowCount() + " / " + duLieu.size() + " bản ghi");
        if (vungBang != null) theBang.show(vungBang, duLieu.isEmpty() ? "rong" : "bang");
    }

    protected int hangChon() {
        int r = bang.getSelectedRow();
        return r < 0 ? -1 : bang.convertRowIndexToModel(r);
    }

    protected String gt(Object[] r, String khoa) {
        for (int i = 0; i < cot.length; i++)
            if (khoa.equals(cot[i].khoa) || khoa.equals(cot[i].db)) return String.valueOf(r[i]);
        return "";
    }

    /* ==================== THÊM / SỬA / XOÁ ==================== */
    protected void moForm(int hang) {
        boolean them = hang < 0;
        Object[] cu = them ? null : duLieu.get(hang);
        JComponent[] o = new JComponent[cot.length];

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        for (int i = 0; i < cot.length; i++) {
            Cot c = cot[i];
            if (!c.suaDuoc || c.db == null) continue;
            JPanel o1 = new JPanel(new BorderLayout(0, 6));
            o1.setOpaque(false);
            o1.setAlignmentX(Component.LEFT_ALIGNMENT);
            o1.setMaximumSize(new Dimension(9999, 70));
            o1.setBorder(new EmptyBorder(0, 0, 12, 0));
            o1.add(Giao.chu(c.ten, 11, Font.BOLD, Giao.CHU_PHU), BorderLayout.NORTH);
            JComponent in;
            String[] ds = c.batTat != null ? c.batTat : c.chon;
            if (ds != null && ds.length > 0) {
                JComboBox<String> cb = new JComboBox<>(ds);
                if (cu != null) cb.setSelectedItem(String.valueOf(cu[i]));
                cb.setFont(Giao.f(13, Font.PLAIN));
                cb.setBackground(Color.WHITE);
                cb.setForeground(Giao.CHU);
                in = cb;
            } else {
                Giao.O tf = new Giao.O("", false);
                tf.setText(cu == null ? "" : String.valueOf(cu[i]));
                in = tf;
            }
            in.setPreferredSize(new Dimension(300, 40));
            o[i] = in;
            o1.add(in, BorderLayout.CENTER);
            form.add(o1);
        }

        Giao.HopThoai ht = new Giao.HopThoai(this,
                (them ? "Thêm " : "Cập nhật ") + tenThucThe(),
                them ? "Điền thông tin để tạo bản ghi mới" : "Chỉnh sửa thông tin bản ghi đang chọn",
                form, them ? "Tạo mới" : "Lưu thay đổi", Giao.Nut.CHINH_NUT);
        if (!ht.moVaCho()) return;

        Object[] moi = new Object[cot.length];
        for (int i = 0; i < cot.length; i++) {
            if (o[i] == null) moi[i] = cu != null ? cu[i] : "";
            else if (o[i] instanceof JComboBox) moi[i] = String.valueOf(((JComboBox<?>) o[i]).getSelectedItem());
            else moi[i] = ((JTextField) o[i]).getText().trim();
        }
        tinhThem(moi);
        if (!luuCSDL(moi, them ? null : khoaHang.get(hang))) return;
        if (dungCSDL) taiLai();
        else {
            if (them) { duLieu.add(0, moi); khoaHang.add(0, null); }
            else duLieu.set(hang, moi);
            veLai();
        }
        Giao.bao(this, them ? "Đã thêm " + tenThucThe() + " mới" : "Đã cập nhật " + tenThucThe(), Giao.LUC);
    }

    private void xoa() {
        int r = hangChon();
        if (r < 0) { Giao.bao(this, "Hãy chọn một dòng trước", Giao.CAM); return; }
        JPanel than = new JPanel(new BorderLayout());
        than.setOpaque(false);
        than.add(Giao.chu("<html><div style='width:340px'>Bản ghi sẽ bị xoá vĩnh viễn khỏi cơ sở dữ liệu "
                + "và không thể khôi phục.</div></html>", 13, Font.PLAIN, Giao.CHU), BorderLayout.NORTH);
        Giao.HopThoai h = new Giao.HopThoai(this, "Xoá " + tenThucThe() + "?",
                "Thao tác này không thể hoàn tác", than, "Xoá vĩnh viễn", Giao.Nut.NGUY);
        if (!h.moVaCho()) return;
        if (!xoaCSDL(khoaHang.get(r))) return;
        duLieu.remove(r);
        khoaHang.remove(r);
        veLai();
        Giao.bao(this, "Đã xoá " + tenThucThe(), Giao.DO);
    }

    private void xuatCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(tenThucThe().replace(" ", "_") + ".csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (Writer w = new OutputStreamWriter(new FileOutputStream(fc.getSelectedFile()), StandardCharsets.UTF_8)) {
            w.write('\ufeff');
            for (int i = 0; i < cot.length; i++) w.write((i > 0 ? ";" : "") + cot[i].ten);
            w.write("\n");
            for (Object[] r : duLieu) {
                for (int i = 0; i < cot.length; i++)
                    w.write((i > 0 ? ";" : "") + "\"" + String.valueOf(r[i]).replace("\"", "\"\"") + "\"");
                w.write("\n");
            }
            Giao.bao(this, "Đã xuất CSV, mở được bằng Excel", Giao.LUC);
        } catch (Exception e) {
            Giao.bao(this, "Lỗi xuất file: " + e.getMessage(), Giao.DO);
        }
    }

    protected void bao(String s) { Giao.bao(this, s, Giao.LAM); }

    protected void khoe(String s, Color m) { Giao.bao(this, s, m); }

    /* ==================== THẺ CHỈ SỐ ==================== */
    protected JPanel the(String nhanThe, String giaTri, String phu, Color mau) {
        return new TheSo(nhanThe, giaTri, phu, mau);
    }

    protected class TheSo extends Giao.The {
        private final String nhanThe, gt, phu, icon;
        private final Color mau;
        private float chay = 0f;
        private double dich = 0;
        private String duoi = "";

        TheSo(String nhanThe, String gt, String phu, Color mau) {
            super(new BorderLayout());
            this.nhanThe = nhanThe;
            this.gt = gt;
            this.phu = phu;
            this.mau = mau;
            this.icon = chonIcon(nhanThe);
            setBorder(new EmptyBorder(16, 18, 16, 18));
            String so = gt.replaceAll("[^0-9]", "");
            if (!so.isEmpty() && so.length() <= 15 && !gt.contains("/")) {
                dich = Double.parseDouble(so);
                duoi = gt.replaceAll("[0-9.,]", "").trim();
                Timer t = new Timer(16, null);
                t.addActionListener(e -> {
                    chay += 0.06f;
                    if (chay >= 1f) { chay = 1f; t.stop(); }
                    repaint();
                });
                t.start();
            } else chay = 1f;
        }

        private String chonIcon(String s) {
            String v = s.toLowerCase();
            if (v.contains("doanh thu") || v.contains("tiền") || v.contains("chi tiêu")
                    || v.contains("giá") || v.contains("lãi") || v.contains("quỹ")
                    || v.contains("giảm")) return "tien";
            if (v.contains("bàn")) return "ban";
            if (v.contains("khách")) return "khach";
            if (v.contains("kho") || v.contains("hàng") || v.contains("nguyên")) return "kho";
            if (v.contains("đơn") || v.contains("giao dịch")) return "don";
            if (v.contains("sao") || v.contains("đánh giá") || v.contains("điểm")) return "sao";
            if (v.contains("nhân") || v.contains("tài khoản") || v.contains("quyền")) return "nhanvien";
            if (v.contains("món") || v.contains("danh mục")) return "mon";
            return "baocao";
        }

        private String hienSo() {
            if (chay >= 1f || dich == 0) return gt;
            long v = (long) (dich * (1 - Math.pow(1 - chay, 3)));
            java.text.DecimalFormatSymbols k = new java.text.DecimalFormatSymbols(java.util.Locale.US);
            k.setGroupingSeparator('.');
            String s = new java.text.DecimalFormat("#,##0", k).format(v);
            return duoi.isEmpty() ? s : s + duoi;
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight() - 3;

            g2.setColor(Giao.nhatHon(mau));
            g2.fillRoundRect(w - 58, 16, 38, 38, 11, 11);
            Giao.Ic.ve(g2, icon, w - 49, 25, 20, mau);

            g2.setFont(Giao.f(11, Font.BOLD));
            g2.setColor(Giao.CHU_PHU);
            g2.drawString(nhanThe.toUpperCase(), 18, 30);

            String sv = hienSo();
            g2.setFont(Giao.f(sv.length() > 12 ? 21 : 25, Font.BOLD));
            g2.setColor(Giao.CHU);
            g2.drawString(sv, 17, 66);

            g2.setFont(Giao.f(11, Font.PLAIN));
            g2.setColor(Giao.CHU_NHAT);
            String p = phu;
            FontMetrics fm = g2.getFontMetrics();
            if (fm.stringWidth(p) > w - 36) {
                while (fm.stringWidth(p + "…") > w - 36 && p.length() > 2) p = p.substring(0, p.length() - 1);
                p += "…";
            }
            g2.drawString(p, 18, h - 16);
            g2.dispose();
        }
    }

    /* ==================== KHAI BÁO CỘT ==================== */
    public static class Cot {
        public static final int THUONG = 0, DAM = 1, TIEN = 2, SO = 3, NHAN = 4;
        public String ten, db, khoa;
        public int rong, kieu;
        public String[] chon, batTat;
        public boolean suaDuoc = true;
        public String fkBang, fkKhoa, fkTen;
        public Map<String, String> idTen, tenId;

        public Cot(String ten, int rong, int kieu) {
            this.ten = ten; this.rong = rong; this.kieu = kieu;
        }
        public Cot db(String c) { this.db = c; if (khoa == null) khoa = c; return this; }
        public Cot khoa(String k) { this.khoa = k; return this; }
        public Cot chon(String... c) { this.chon = c; return this; }
        public Cot batTat(String bat, String tat) { this.batTat = new String[]{bat, tat}; return this; }
        public Cot fk(String bang, String khoaCot, String tenCot) {
            this.fkBang = bang; this.fkKhoa = khoaCot; this.fkTen = tenCot; return this;
        }
        public Cot khoaSua() { this.suaDuoc = false; return this; }
    }

    /* ==================== TIỆN ÍCH ==================== */
    public static JLabel nhan(String s, int co, int kieu, Color m) { return Giao.chu(s, co, kieu, m); }

    public static String tienVN(double v) { return Giao.tien(v); }

    public static double soTu(Object o) {
        try {
            String s = String.valueOf(o).replaceAll("[^0-9,.\\-]", "");
            if (s.contains(".") && s.contains(",")) s = s.replace(".", "").replace(",", ".");
            else if (s.chars().filter(c -> c == '.').count() > 1) s = s.replace(".", "");
            else if (s.matches(".*\\.\\d{3}$")) s = s.replace(".", "");
            else s = s.replace(",", ".");
            return s.isEmpty() ? 0 : Double.parseDouble(s);
        } catch (Exception e) { return 0; }
    }

    public static String font() { return Giao.FONT; }
}