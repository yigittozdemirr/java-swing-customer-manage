package view;

import business.BasketController;
import business.CartController;
import business.CustomerController;
import business.ProductController;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import core.Helper;
import core.Item;
import entity.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DashboardUI extends JFrame {
    private JPanel container;
    private JPanel pnl_top;
    private JLabel lb_welcome;
    private JButton btn_logout;
    private JTabbedPane pnl_basket;
    private JTable tbl_customer;
    private JPanel pnl_customerfilter;
    private JTextField fld_customer_name;
    private JComboBox<Customer.TYPE> cmb_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private JPanel pnl_product;
    private JScrollPane scrl_product;
    private JTable tbl_product;
    private JPanel pnl_f_product;
    private JTextField fld_f_product_name;
    private JTextField fld_product_code;
    private JComboBox<Item> cmb_product_stock;
    private JButton btn_product_search;
    private JButton btn_product_clear;
    private JButton btn_product_add;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private JLabel lbl_product_stock;
    private JPanel pnl_basket_top;
    private JScrollPane scrl_basket;
    private JComboBox<Item> cmb_basket_customer;
    private JButton btn_basket_reset;
    private JButton btn_basket_new;
    private JLabel lbl_basket_price;
    private JLabel lbl_basket_count;
    private JTable tbl_basket;
    private JScrollPane scrl_cart;
    private JTable tbl_cart;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private BasketController basketController;
    private CartController cartController;
    private DefaultTableModel tmdl_customer = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private DefaultTableModel tmdl_product = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private DefaultTableModel tmdl_basket = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private DefaultTableModel tmdl_cart = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JPopupMenu popup_customer = new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();

    public DashboardUI(User user) {
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.basketController = new BasketController();
        this.cartController = new CartController();
        if (user == null) {
            Helper.showMsg("error");
            dispose();
        }

        this.add(container);
        this.setTitle("Müşteri Yönetim Sistemi");
        this.setSize(1000, 500);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);
        this.setVisible(true);

        this.lb_welcome.setText("Hoşgeldin: " + this.user.getName());
        this.btn_logout.addActionListener(e -> {
            dispose();
            LoginUI loginUI = new LoginUI();
        });

        //CUSTOMER TAB
        loadCustomerTable(null);
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();
        this.cmb_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_customer_type.setSelectedItem(null);

        //PRODUCT TAB
        loadProductTable(null);
        loadProductPopupMenu();
        loadProductButtonEvent();
        this.cmb_product_stock.addItem(new Item(1, "Stokta Var"));
        this.cmb_product_stock.addItem(new Item(2, "Stokta Yok"));
        this.cmb_product_stock.setSelectedItem(null);

        //BASKET TAB
        loadBasketTable();
        loadBasketButtonEvent();
        loadBasketCustomerCombo();

        //CART TAB
        loadCartTable();
    }

    private void loadCartTable() {
        Object[] columnCart = {"ID", "Müşteri Adı", "Ürün Adı", "Fiyat", "Sipariş Tarihi", "Not"};
        ArrayList<Cart> carts = this.cartController.findAll();


        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_cart.getModel();
        clearModel.setRowCount(0);

        this.tmdl_cart.setColumnIdentifiers(columnCart);
        for (Cart cart : carts) {
            String customerName = (cart.getCustomer() != null) ? cart.getCustomer().getName() : "Bilinmeyen/Silinmiş Müşteri";
            String productName = (cart.getProduct() != null) ? cart.getProduct().getName() : "Bilinmeyen/Silinmiş Ürün";

            Object[] rowObject = {
                    cart.getId(),
                    customerName,     // Güvenli hale getirdik
                    productName,      // Güvenli hale getirdik
                    cart.getPrice(),
                    cart.getDate(),
                    cart.getNote()
            };
            this.tmdl_cart.addRow(rowObject);
        }

        this.tbl_cart.setModel(tmdl_cart);
        this.tbl_cart.getTableHeader().setReorderingAllowed(false);
        this.tbl_cart.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void loadBasketCustomerCombo() {
        ArrayList<Customer> customers = this.customerController.findAll();
        this.cmb_basket_customer.removeAllItems();
        for (Customer customer : customers) {
            int comboKey = customer.getId();
            String comboValue = customer.getName();
            this.cmb_basket_customer.addItem(new Item(comboKey, comboValue));
        }
        this.cmb_basket_customer.setSelectedItem(null);
    }

    private void loadBasketButtonEvent() {
        this.btn_basket_reset.addActionListener(e -> {
            if (this.basketController.clear()) {
                Helper.showMsg("done");
                loadBasketTable();
            } else {
                Helper.showMsg("error");
            }
        });

        this.btn_basket_new.addActionListener(e -> {
            Item selectedCustomer = (Item) this.cmb_basket_customer.getSelectedItem();
            if (selectedCustomer == null) {
                Helper.showMsg("Lütfen bir müşteri seçiniz!");
            } else {
                Customer customer = this.customerController.getById(selectedCustomer.getKey());
                ArrayList<Basket> baskets = this.basketController.findAll();
                if (customer.getId() == 0) {
                    Helper.showMsg("Böyle bir müşteri bulunamadı!");
                } else if (baskets.size() == 0) {
                    Helper.showMsg("Lütfen sepete ürün ekleyiniz!");
                } else {
                    CartUI cartUI = new CartUI(customer);
                    cartUI.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            loadBasketTable();
                            loadProductTable(null);
                            loadCartTable();
                        }
                    });
                }
            }
        });
    }

    private void loadBasketTable() {
        Object[] columnBasket = {"ID", "Ürün Adı", "Ürün Kodu", "Fiyat", "Stock"};
        ArrayList<Basket> baskets = basketController.findAll();


        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_basket.getModel();
        clearModel.setRowCount(0);

        this.tmdl_basket.setColumnIdentifiers(columnBasket);
        int totalPrice = 0;
        for (Basket basket : baskets) {
            Object[] rowObject = {
                    basket.getId(),
                    basket.getProduct().getName(),
                    basket.getProduct().getCode(),
                    basket.getProduct().getPrice(),
                    basket.getProduct().getStock()
            };
            this.tmdl_basket.addRow(rowObject);

            totalPrice += basket.getProduct().getPrice();
        }

        this.lbl_basket_price.setText(totalPrice + " TL");
        this.lbl_basket_count.setText(baskets.size() + " Adet");

        this.tbl_basket.setModel(tmdl_basket);
        this.tbl_basket.getTableHeader().setReorderingAllowed(false);
        this.tbl_basket.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void loadProductButtonEvent() {
        btn_product_add.addActionListener(e -> {
            ProductUI productUI = new ProductUI(new Product());
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });

        btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.filter(
                    this.fld_customer_name.getText(),
                    (Customer.TYPE) cmb_customer_type.getSelectedItem()
            );
            loadCustomerTable(filteredCustomers);
        });

        btn_customer_filter_reset.addActionListener(e -> {
            loadCustomerTable(null);
            this.fld_customer_name.setText(null);
            this.cmb_customer_type.setSelectedItem(null);
        });

        btn_product_search.addActionListener(e -> {
            ArrayList<Product> filteredProducts = this.productController.filter(
                    this.fld_f_product_name.getText(),
                    this.fld_product_code.getText(),
                    (Item) this.cmb_product_stock.getSelectedItem()
            );
            loadProductTable(filteredProducts);
        });

        btn_product_clear.addActionListener(e -> {
            this.fld_f_product_name.setText(null);
            this.fld_product_code.setText(null);
            this.cmb_product_stock.setSelectedItem(null);
            loadProductTable(null);
        });

    }

    private void loadProductPopupMenu() {
        this.tbl_product.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_product.rowAtPoint(e.getPoint());
                if (selectedRow >= 0) {
                    tbl_product.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        });

        this.popup_product.add("Sepete Ekle").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(), 0).toString());
            Product basketProduct = this.productController.getById(selectId);
            if (basketProduct.getStock() <= 0) {
                Helper.showMsg("Bu ürün stokta yoktur!");
            } else {
                Basket basket = new Basket(basketProduct.getId());
                if (this.basketController.save(basket)) {
                    Helper.showMsg("done");
                    loadBasketTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        this.popup_product.add("Güncelle").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(), 0).toString());
            ProductUI productUI = new ProductUI(this.productController.getById(selectId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadBasketTable();
                }
            });
        });

        this.popup_product.add("Sil").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(), 0).toString());
            if (Helper.confirm("sure")) {
                if (this.productController.delete(selectId)) {
                    Helper.showMsg("done");
                    loadProductTable(null);
                    loadBasketTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        ;

        this.tbl_product.setComponentPopupMenu(this.popup_product);

    }

    private void loadProductTable(ArrayList<Product> products) {
        Object[] columnProduct = {"ID", "Ürün Adı", "Ürün Kodu", "Fiyat", "Stock"};

        if (products == null) {
            products = this.productController.findAll();
        }

        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_product.getModel();
        clearModel.setRowCount(0);

        this.tmdl_product.setColumnIdentifiers(columnProduct);
        for (Product product : products) {
            Object[] rowObject = {
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock()
            };
            this.tmdl_product.addRow(rowObject);
        }

        this.tbl_product.setModel(tmdl_product);
        this.tbl_product.getTableHeader().setReorderingAllowed(false);
        this.tbl_product.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    private void loadCustomerButtonEvent() {
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });

        btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.filter(
                    this.fld_customer_name.getText(),
                    (Customer.TYPE) cmb_customer_type.getSelectedItem()
            );
            loadCustomerTable(filteredCustomers);
        });

        btn_customer_filter_reset.addActionListener(e -> {
            loadCustomerTable(null);
            this.fld_customer_name.setText(null);
            this.cmb_customer_type.setSelectedItem(null);
        });
    }

    public void loadCustomerPopupMenu() {
        this.tbl_customer.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_customer.rowAtPoint(e.getPoint());
                if (selectedRow >= 0) {
                    tbl_customer.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        });
        this.popup_customer.add("Güncelle").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            CustomerUI customerUI = new CustomerUI(this.customerController.getById(selectId));
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });

        });
        this.popup_customer.add("Sil").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            if (Helper.confirm("sure")) {
                if (this.customerController.delete(selectId)) {
                    Helper.showMsg("done");
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_customer.setComponentPopupMenu(this.popup_customer);
    }

    private void loadCustomerTable(ArrayList<Customer> customers) {
        Object[] columnCustomer = {"ID", "Müşteri Adı", "Tipi", "Telefon", "E-Posta", "Adres"};

        if (customers == null) {
            customers = this.customerController.findAll();
        }

        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_customer.getModel();
        clearModel.setRowCount(0);
        this.tmdl_customer.setColumnIdentifiers(columnCustomer);
        for (Customer customer : customers) {
            Object[] rowObject = {customer.getId(),
                    customer.getName(),
                    customer.getType(),
                    customer.getPhone(),
                    customer.getAdress()
            };
            this.tmdl_customer.addRow(rowObject);
        }

        this.tbl_customer.setModel(tmdl_customer);
        this.tbl_customer.getTableHeader().setReorderingAllowed(false);
        this.tbl_customer.getColumnModel().getColumn(0).setMaxWidth(50);


    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        container = new JPanel();
        container.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        container.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        pnl_top = new JPanel();
        pnl_top.setLayout(new GridLayoutManager(1, 2, new Insets(10, 5, 10, 5), -1, -1));
        container.add(pnl_top, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lb_welcome = new JLabel();
        lb_welcome.setText("Hoşgeldiniz");
        pnl_top.add(lb_welcome, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_logout = new JButton();
        btn_logout.setText("Çıkış Yap");
        pnl_top.add(btn_logout, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pnl_basket = new JTabbedPane();
        container.add(pnl_basket, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1, true, false));
        pnl_basket.addTab("Müşteriler", panel1);
        pnl_customerfilter = new JPanel();
        pnl_customerfilter.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(pnl_customerfilter, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lbl_f_customer_name = new JLabel();
        lbl_f_customer_name.setText("Müşteri Adı");
        pnl_customerfilter.add(lbl_f_customer_name, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        pnl_customerfilter.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        pnl_customerfilter.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lbl_f_customer_type = new JLabel();
        lbl_f_customer_type.setText("Müşteri Tipi");
        panel2.add(lbl_f_customer_type, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmb_customer_type = new JComboBox();
        pnl_customerfilter.add(cmb_customer_type, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_customer_filter = new JButton();
        btn_customer_filter.setText("Arama Yap");
        pnl_customerfilter.add(btn_customer_filter, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_customer_filter_reset = new JButton();
        btn_customer_filter_reset.setText("Temizle");
        pnl_customerfilter.add(btn_customer_filter_reset, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_customer_new = new JButton();
        btn_customer_new.setText("Yeni Ekle");
        pnl_customerfilter.add(btn_customer_new, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fld_customer_name = new JTextField();
        pnl_customerfilter.add(fld_customer_name, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tbl_customer = new JTable();
        panel1.add(tbl_customer, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        pnl_product = new JPanel();
        pnl_product.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        pnl_basket.addTab("Ürünler", pnl_product);
        scrl_product = new JScrollPane();
        pnl_product.add(scrl_product, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tbl_product = new JTable();
        scrl_product.setViewportView(tbl_product);
        pnl_f_product = new JPanel();
        pnl_f_product.setLayout(new GridLayoutManager(3, 6, new Insets(0, 0, 0, 0), -1, -1, true, false));
        pnl_product.add(pnl_f_product, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lbl_f_product_name = new JLabel();
        lbl_f_product_name.setText("Ürün Adı");
        pnl_f_product.add(lbl_f_product_name, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        pnl_f_product.add(spacer2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        pnl_f_product.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fld_f_product_name = new JTextField();
        pnl_f_product.add(fld_f_product_name, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lbl_f_product_code = new JLabel();
        lbl_f_product_code.setText("Ürün Kodu");
        pnl_f_product.add(lbl_f_product_code, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fld_product_code = new JTextField();
        pnl_f_product.add(fld_product_code, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lbl_product_stock = new JLabel();
        lbl_product_stock.setText("Label");
        pnl_f_product.add(lbl_product_stock, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmb_product_stock = new JComboBox();
        pnl_f_product.add(cmb_product_stock, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_product_search = new JButton();
        btn_product_search.setText("Arama Yap");
        pnl_f_product.add(btn_product_search, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_product_clear = new JButton();
        btn_product_clear.setText("Temizle");
        pnl_f_product.add(btn_product_clear, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_product_add = new JButton();
        btn_product_add.setText("Yeni Ekle");
        pnl_f_product.add(btn_product_add, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        pnl_basket.addTab("Sipariş Oluştur", panel3);
        pnl_basket_top = new JPanel();
        pnl_basket_top.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel3.add(pnl_basket_top, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Müşteri Seçiniz");
        pnl_basket_top.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        pnl_basket_top.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cmb_basket_customer = new JComboBox();
        pnl_basket_top.add(cmb_basket_customer, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Toplam Tutar");
        pnl_basket_top.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbl_basket_price = new JLabel();
        lbl_basket_price.setText("0 TL");
        pnl_basket_top.add(lbl_basket_price, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Ürün Sayısı");
        pnl_basket_top.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbl_basket_count = new JLabel();
        lbl_basket_count.setText("0 Adet");
        pnl_basket_top.add(lbl_basket_count, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_basket_reset = new JButton();
        btn_basket_reset.setText("Temizle");
        pnl_basket_top.add(btn_basket_reset, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_basket_new = new JButton();
        btn_basket_new.setText("Sipariş Oluştur");
        pnl_basket_top.add(btn_basket_new, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrl_basket = new JScrollPane();
        panel3.add(scrl_basket, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tbl_basket = new JTable();
        scrl_basket.setViewportView(tbl_basket);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        pnl_basket.addTab("Siparişler", panel4);
        scrl_cart = new JScrollPane();
        panel4.add(scrl_cart, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tbl_cart = new JTable();
        scrl_cart.setViewportView(tbl_cart);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return container;
    }

}
