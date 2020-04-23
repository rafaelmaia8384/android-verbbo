package br.com.globalinotec.verbbo;

import android.content.Context;
import android.util.Log;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    static public String limparCPF(String cpf) {

        return cpf.replaceAll("[^x0-9]", "");
    }

    static public String limparMatricula(String matricula) {

        return matricula.replaceAll("[^x0-9]", "");
    }

    static public String formatarCPF(String cpf) {

        String result;

        if (cpf.length() == 11) {

            result = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
        }
        else if (cpf.length() == 10) {

            result = "0" + cpf.substring(0, 2) + "." + cpf.substring(2, 5) + "." + cpf.substring(5, 8) + "-" + cpf.substring(8, 10);
        }
        else if (cpf.length() == 9) {

            result = "00" + cpf.substring(0, 1) + "." + cpf.substring(1, 4) + "." + cpf.substring(4, 7) + "-" + cpf.substring(7, 9);
        }
        else if (cpf.length() == 8) {

            result = "000." + cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "-" + cpf.substring(6, 8);
        }
        else {

            result = cpf;
        }

        return result;
    }

    static public String formatarMatricula(String matricula) {

        matricula = matricula.replaceAll("[^x0-9]", "");

        return matricula.substring(0, 3) + "." + matricula.substring(3, 6) + "-" + matricula.substring(6, 7);
    }

    static public String formatarMarcaDagua(String matricula) {

        if (matricula == null || matricula.length() == 0) return "";

        String result = "";
        String marcaDagua = matricula.charAt(0) + " " + matricula.charAt(1) + " " + matricula.charAt(2) + " " + matricula.charAt(3) + " " + matricula.charAt(4) + " " + matricula.charAt(5) + " - " + matricula.charAt(6) + " ";

        for (int i = 0; i < 1000; i++) {

            result += marcaDagua;
        }

        return result;
    }

    static public boolean validarPlaca(String placa) {

        Pattern pattern = Pattern.compile("[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}|[A-Z]{3}[0-9]{4}");
        Matcher matcher = pattern.matcher(placa.toUpperCase());

        return matcher.matches();
    }

    static public boolean validarCPF(String cpfText) {

        boolean ret = false;
        String base = "000000000";
        String digitos = "00";

        String cpf = cpfText.replaceAll("[^0-9]", "");

        if (cpf.length() == 0) return false;

        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333") || cpf.equals("44444444444") ||
                cpf.equals("55555555555") || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") || cpf.equals("99999999999")) {

            return false;
        }

        if (cpf.length() <= 11) {

            if (cpf.length() < 11) {

                cpf = base.substring(0, 11 - cpf.length()) + cpf;
                base = cpf.substring(0, 9);
            }

            base = cpf.substring(0, 9);
            digitos = cpf.substring(9, 11);

            int soma = 0, mult = 11;
            int[] var = new int[11];


            for (int i = 0; i < 9; i++) {

                var[i] = Integer.parseInt("" + cpf.charAt(i));

                if (i < 9) {

                    soma += (var[i] * --mult);
                }
            }

            int resto = soma % 11;

            if (resto < 2) {

                var[9] = 0;
            }
            else {

                var[9] = 11 - resto;
            }

            soma = 0;
            mult = 11;

            for (int i = 0; i < 10; i++) {

                soma += var[i] * mult--;
            }

            resto = soma % 11;

            if (resto < 2) {

                var[10] = 0;
            }
            else {

                var[10] = 11 - resto;
            }

            if ((digitos.substring(0, 1).equalsIgnoreCase(Integer.toString(var[9]))) && (digitos.substring(1, 2).equalsIgnoreCase(Integer.toString(var[10])))) {

                ret = true;
            }
        }

        return ret;
    }

    public static boolean validarNome(String name) {

        if (name.length() < 5) return false;
        if (name.split(" ").length < 2) return false;

        String regx = "^[\\p{L} .'-]+$";

        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);

        return matcher.find();
    }

    public static boolean validarEmail(String email) {

        return email.length() > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validarMatricula(String matricula) {

        String m = matricula.replaceAll("[^\\d]", "");
        String dv = m.substring(m.length()-1);

        m = m.substring(0, m.length()-1);

        return Integer.toString(modulo11(m)).equals(dv);
    }

    private static int modulo11(String chave) {

        int total = 0;
        int peso = 2;

        for (int i = 0; i < chave.length(); i++) {
            total += (chave.charAt((chave.length()-1) - i) - '0') * peso;
            peso++;
            if (peso == 10)
                peso = 2;
        }
        int resto = total % 11;

        return (resto == 0 || resto == 1) ? 0 : (11 - resto);
    }

    public static String formatarTexto(String text) {

        String result = text.replaceAll("(?m)(^ *| +(?= |$))", "").replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1");

        return result.trim();
    }

    public static String formatarData(String data) {

        SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat saida = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss");

        try {

            Date dataEntrada = entrada.parse(data);

            return saida.format(dataEntrada);
        }
        catch (Exception e) {

            Log.d("SAI ERROR: ", e.getLocalizedMessage());

            return "erro";
        }
    }

    public static int dpToPx(Context context, int dp) {

        float density = context.getResources()
                .getDisplayMetrics()
                .density;

        return Math.round((float) dp * density);
    }

    public static String formatarDataSimple(String data) {

        SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat saida = new SimpleDateFormat("dd/MM/yyyy");

        try {

            Date dataEntrada = entrada.parse(data);

            return saida.format(dataEntrada);
        }
        catch (Exception e) {

            Log.d("SAI ERROR: ", e.getLocalizedMessage());

            return "erro";
        }
    }

    public static String getMacAddr() {

        try {

            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface nif : all) {

                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();

                if (macBytes == null) {

                    return "";
                }

                StringBuilder res1 = new StringBuilder();

                for (byte b : macBytes) {

                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {

                    res1.deleteCharAt(res1.length() - 1);
                }

                return res1.toString();
            }
        }
        catch (Exception ex) { }

        return "02:00:00:00:00:00";
    }
}
