package core;

import javax.swing.*;

public class Helper {

    public static void setTheme(){
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if (info.getName().equals("Nimbus")){
                try{
                    UIManager.setLookAndFeel((info.getClassName()));
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                } catch (InstantiationException e){
                    e.printStackTrace();
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field){
        return field.getText().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fields){
        for(JTextField field : fields){
            if(isFieldEmpty(field)) return true;
        }

        return false;
    }

    public static boolean isEmailValid(String mail){
        if(mail == null || mail.trim().isEmpty()) return false;

        if(!mail.contains("@")) return false;

        String[] parts = mail.split("@");
        if(parts.length != 2) return false;

        if(parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) return false;

        if(!parts[1].contains(".")) return false;

        return true;
    }

    public static void optionPaneDialogTR(){
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }

    public static void showMsg(String message){
        String msg;
        String title;
        optionPaneDialogTR();
        title = switch (message) {
            case "fill" -> {
                msg = "Lütfen tüm alanları doldurunuz!";
                yield "HATA";
            }
            case "done" -> {
                msg = "İşlem Başarılı!";
                yield "Sonuç";
            }
            case "error" -> {
                msg = "Bir hata oluştu!";
                yield "HATA";
            }
            default -> {
                msg = message;
                yield "Mesaj";
            }
        };

        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str){
        optionPaneDialogTR();
        String msg;
        if(str.equals("sure")){
            msg = "Emin misiniz?";
        }else{
            msg = str;
        }

        return JOptionPane.showConfirmDialog(null, msg, "Emin Misin?", JOptionPane.YES_NO_OPTION) == 0;
    }

}
