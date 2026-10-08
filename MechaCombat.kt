import kotlin.math.max

// Representa o estado do robô de combate
class RoboCombate(
    val nome: String,
    var vida: Int = 100,
    var energia: Int = 100,
    var temperatura: Int = 30,
    var escudosAtivos: Int = 0
)

fun main() {
    println("==========================================")
    println("   BEM-VINDO AO COMBAT ROBOT MANAGER   ")
    println("==========================================")

    print("Digite o nome do seu Robô: ")
    // Null Safety: operador elvis (?:) garante um nome padrão caso a entrada seja nula/vazia
    val nomeRobo = readlnOrNull()?.takeIf { it.isNotBlank() } ?: "Mecha-01"
    val robo = RoboCombate(nome = nomeRobo)

    var vidaInimigo = 120
    var emCombate = true

    println("\n[SISTEMA]: Robô '${robo.nome}' inicializado com sucesso!")

    // Laço de Repetição (while) para manter o menu ativo até o fim do combate
    while (emCombate && robo.vida > 0 && vidaInimigo > 0) {
        exibirStatus(robo, vidaInimigo)

        // Verificação de Condição: alerta de superaquecimento
        if (robo.temperatura >= 80) {
            println("\n⚠️  [ALERTA CRÍTICO]: NÚCLEO SUPERAQUECIDO (${robo.temperatura}°C)! RESFRIE IMEDIATAMENTE OU O ROBÔ EXPLODIRÁ!")
        }

        exibirMenu()
        print("Escolha uma ação (1-4): ")
        val entrada = readlnOrNull()

        // Estrutura de Condição (when) para controlar as opções
        when (entrada) {
            "1" -> {
                // Transferência de energia para o Ataque
                print("Quanto de energia deseja direcionar para as ARMAS? ")
                val energiaGasta = lerNumeroUsuario()
                atacar(robo, energiaGasta, vidaInimigo).let { vidaInimigo = it }
            }
            "2" -> {
                // Balanceamento de energia para Escudos
                print("Quanto de energia deseja direcionar para os ESCUDOS? ")
                val energiaGasta = lerNumeroUsuario()
                carregarEscudo(robo, energiaGasta)
            }
            "3" -> {
                // Resfriamento do núcleo
                resfriarNucleo(robo)
            }
            "4" -> {
                println("\n[SISTEMA]: Você ativou a retirada estratégica. Fim de jogo!")
                emCombate = false
            }
            else -> println("\n⚠️  Opção inválida! Escolha um número entre 1 e 4.")
        }

        // Se o inimigo ainda estiver vivo e o combate continuar, o inimigo ataca
        if (emCombate && vidaInimigo > 0) {
            turnosInimigo(robo)
        }

        // Condição de derrota por superaquecimento excessivo
        if (robo.temperatura >= 100) {
            println("\n💥 [EXPLOSÃO]: O núcleo do '${robo.nome}' derreteu devido ao calor extremo (${robo.temperatura}°C)!")
            robo.vida = 0
        }
    }

    // Estrutura de Condição (if/else) para o resultado final
    println("\n==========================================")
    if (robo.vida <= 0) {
        println("🛑 FIM DE JOGO: O robô '${robo.nome}' foi destruído.")
    } else if (vidaInimigo <= 0) {
        println("🏆 VITÓRIA! Você destruiu o robô inimigo!")
    }
    println("==========================================")
}

// FUNÇÕES DE LÓGICA E SUPORTE

// Função para tratar a leitura de números com Null Safety
fun lerNumeroUsuario(): Int {
    val entrada = readlnOrNull()
    // Safe Call (?.) + Operador Elvis (?:)
    return entrada?.toIntOrNull() ?: 0
}

// Função de exibição de status
fun exibirStatus(robo: RoboCombate, vidaInimigo: Int) {
    println("\n------------------------------------------")
    println("STATUS DO ROBÔ: ${robo.nome}")
    println("Vida: ${robo.vida}/100 | Energia: ${robo.energia}/100 | Temp: ${robo.temperatura}°C | Escudos: ${robo.escudosAtivos}")
    println("VIDA DO INIMIGO: $vidaInimigo/120")
    println("------------------------------------------")
}

fun exibirMenu() {
    println("1 - Atacar (Gasta Energia, Gera Calor)")
    println("2 - Fortalecer Escudos (Gasta Energia, Reduz Dano Rebatido)")
    println("3 - Resfriar Núcleo (Recupera Energia, Reduz Temperatura)")
    println("4 - Fugir da Batalha")
}

// Função com operadores matemáticos para o ataque
fun atacar(robo: RoboCombate, energiaAtaque: Int, vidaInimigoAtual: Int): Int {
    // Operadores relacionais e lógicos (&&)
    if (energiaAtaque <= 0 || robo.energia < energiaAtaque) {
        println("❌ [ERRO]: Energia insuficiente para realizar o ataque!")
        return vidaInimigoAtual
    }

    // Cálculo matemático: Dano é proporcional à energia aplicada
    val danoGerado = energiaAtaque * 2
    val aumentoTemperatura = energiaAtaque / 2

    robo.energia -= energiaAtaque
    robo.temperatura += aumentoTemperatura

    val novaVidaInimigo = max(0, vidaInimigoAtual - danoGerado)

    println("💥 [ATAQUE]: Você causou $danoGerado de dano! (Consumiu $energiaAtaque de energia e gerou ${aumentoTemperatura}°C de calor)")
    return novaVidaInimigo
}

// Função para gerenciamento do escudo
fun carregarEscudo(robo: RoboCombate, energiaEscudo: Int) {
    if (energiaEscudo <= 0 || robo.energia < energiaEscudo) {
        println("❌ [ERRO]: Energia insuficiente para carregar os escudos!")
        return
    }

    robo.energia -= energiaEscudo
    robo.escudosAtivos += energiaEscudo / 2
    robo.temperatura += 5 // Pequeno custo térmico

    println("🛡️  [ESCUDO]: Escudos reforçados em +${energiaEscudo / 2} pontos!")
}

// Função para resfriamento do sistema
fun resfriarNucleo(robo: RoboCombate) {
    println("❄️  [RESFRIAMENTO]: Ativando jatos de nitrogênio líquido...")

    // Operadores matemáticos (+, -)
    robo.temperatura = max(20, robo.temperatura - 40)
    robo.energia = minOf(100, robo.energia + 30)

    println("🌡️  Temperatura reduzida para ${robo.temperatura}°C. Energia recarregada em +30!")
}

// Contra-ataque do inimigo
fun turnosInimigo(robo: RoboCombate) {
    val danoBaseInimigo = 25
    println("\n🔴 [TURNO INIMIGO]: O robô inimigo está disparando!")

    // Lógica do Escudo absorvendo dano
    if (robo.escudosAtivos > 0) {
        if (robo.escudosAtivos >= danoBaseInimigo) {
            robo.escudosAtivos -= danoBaseInimigo
            println("🛡️  Seu escudo absorveu TODO o dano do ataque!")
        } else {
            val danoRestante = danoBaseInimigo - robo.escudosAtivos
            println("🛡️  Escudo destruído! Absorveu ${robo.escudosAtivos} de dano.")
            robo.escudosAtivos = 0
            robo.vida -= danoRestante
            println("⚠️  Seu robô recebeu $danoRestante de dano residual na estrutura.")
        }
    } else {
        robo.vida -= danoBaseInimigo
        println("💥 Seu robô sofreu $danoBaseInimigo de dano direto!")
    }
}