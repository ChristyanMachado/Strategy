package br.edu.umfg.estrategia;

import java.util.regex.Pattern;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MeioPagamentoCieloCartaoDebitoEstrategia implements MeioPagamentoEstrategia {
    private String numeroCartao;
    private String cpf;
    private String cvv;
    private String dataValidade;

    public MeioPagamentoCieloCartaoDebitoEstrategia(String numeroCartao,
                                                    String cpf, String cvv,
                                                    String dataValidade) {
        this.numeroCartao = numeroCartao;
        this.cpf = cpf;
        this.cvv = cvv;
        this.dataValidade = dataValidade;
    }

    @Override
    public void pagar(Double valor) {
        if (validarCartaoDebito()) {
            System.out.printf("Pagamento via Cielo (Cartão de Débito) no valor %.2f realizado com sucesso.\n", valor);
        } else {
            System.out.println("Falha no pagamento via Cielo (Cartão de Débito). Dados inválidos.");
        }
    }

    private boolean validarCartaoDebito() {
        if (!validarNumeroCartaoDeDebito(numeroCartao)) {
            return false;
        }

        if (!validarCPF(cpf)) {
            return false;
        }

        if (!validarCVV(cvv)) {
            return false;
        }

        if (!validarDataValidade(dataValidade)) {
            return false;
        }

        return true;
    }

    private boolean validarNumeroCartaoDeDebito(String numeroCartao) {
        //Cartão de débito válido se tiver 16 dígitos
        return numeroCartao != null && numeroCartao.matches("^\\d{16}$");
    }

    private boolean validarCPF(String cpf) {
        //CPF válido se tiver 11 dígitos
        if (cpf != null && cpf.matches("^\\d{11}$")) {
            // Verifica CPFs com sequências de números iguais
            if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
                    cpf.equals("22222222222") || cpf.equals("33333333333") ||
                    cpf.equals("44444444444") || cpf.equals("55555555555") ||
                    cpf.equals("66666666666") || cpf.equals("77777777777") ||
                    cpf.equals("88888888888") || cpf.equals("99999999999")) {
                return false;
            }

            // Cálculo dos dígitos verificadores
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                int num = Character.getNumericValue(cpf.charAt(i));
                soma += num * peso;
                peso--;
            }

            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) {
                primeiroDigito = 0;
            }

            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                int num = Character.getNumericValue(cpf.charAt(i));
                soma += num * peso;
                peso--;
            }

            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) {
                segundoDigito = 0;
            }

            // Verifica se os dígitos calculados conferem com os dígitos informados
            return primeiroDigito == Character.getNumericValue(cpf.charAt(9)) &&
                    segundoDigito == Character.getNumericValue(cpf.charAt(10));
        }
        return true;
    }

    private boolean validarCVV(String cvv) {
        //CVV válido se tiver 3 dígitos
        return cvv != null && cvv.matches("^\\d{3}$");
    }

    private boolean validarDataValidade(String dataValidade) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            YearMonth validade = YearMonth.parse(dataValidade, formatter);

            YearMonth hoje = YearMonth.now();


            return validade.isAfter(hoje);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
