# Resumo Lab cloud Azure

## Custos
Existem muitos recursos e serviços disponíveis, uns de maneira gratuita para sempre, outros gratuitos por 12 meses e outros mediante consumo, onde a Microsoft disponibiliza US$ 200.00 por 30 dias para experimentá-los.

## Modelos
Existem 4 modelos de nuvem:
- **Privada**: infraestrutura proprietária de uma empresa;
- **Pública**: infraestrutura pertence a um provedor e é gerenciada por uma empresa/usuário cliente;
- **Híbrida**: as duas formas cima coexistindo simultaneamente;
- **Multicloud**: possui mais de um fornecedor de nuvem pública

## Laboratório
Ao criar a conta, é disponibilizado um ambiente com acesso aos recursos/serviços disponíveis. Nesse ambiente é possível realizar várias personalizações. Além disso, é possível verificar a descrição de cada serviço para entender o seu objetivo.
Outro ponto é que existem alguns serviços listados como versão prévia. Estes serviços podem ou não seram efetivados, então é necessário ter cautela ao utilizá-los em um ambiente de produção ,pois pode ocasionar problemas irreparáveis à organização.

## SLA
O SLA refere-se a métrica de indisponibilidade permitida em contrato. Dessa forma, quanto maior o acordo de SLA, menor é a indisponibilidade dos serviços, ou seja, o provedor se prontifica a manter os serviços disponível baseado nessa cláusula contratual. Consequentemente, isso implica nos custos do contrato, pois o provedor precisará usar mais recursos para garantira tal disponibilidade.

## Escalabilidade
A escalabilidade é um termo muito importante no mundo cloud, pois é um dos mais notórios benefícios para a sua adoção. A possibilidade de aumentar/diminuir os recursos conforme a demanda é, sem dúvidas, um atrativo para esse modelo, tendo em vista que torna os custos mais previsíveis e mensuráveis.

## Modelos de Serviço
### IaaS
Esse modelo o provedor disponibiliza uma estrutura básica para que o usuário (cliente) seja o responsável pelo gerenciamento de recursos e serviços ao quais ele venha a necessitar. Basicamente são disponíveis estruturas físicas para o cliente.

### PaaS
Esse modelo oferece uma gestão compartilhada entre provedor e cliente. Isso facilita muitas situações para o usuário pois oferece um nível de configurações menor. Aqui entra em ação um sistema operacional que faz com que esse modelo seja mais "fácil" de gerenciar pelo usuário.

### SaaS
Já para esse modelo, o provedor entra com a maior parte dos serviços gerenciados, facilitando ainda mais o gerenciamento por parte do usuário. Isso porque com esse nível, basicamente o usuário foca mais nas configurações voltadas ao seu negócio.

## Regiões
As regiões são compostas de um ou mais datacenters muito próximos. Elas fornecem flexibilidade e escala para reduzir a latência do cliente. As regiões preservam a residência dos dados com uma oferta abrangente de conformidade.

## Zonas de disponibilidade
Fornece proteção contra tempo de inatividade devido a falha do datacenter. Separa fisicamente os datacenters dentro da mesma região. Cada datacenter é equipado com alimentação, resfriamento e rede independentes. São conectadas por meio de redes privadas de fibra óptica.

## Pares de região
No mínimo 300 milhas de separação entre pares de regiões. Replicação automática para alguns serviços. Recuperação de região priorizada em caso de interrupção.

![image](https://github.com/user-attachments/assets/b0847984-f5c3-42c2-985b-6c605135c0bc)

https://aka.ms/PairedRegions-ptb

## Recursos
![image](https://github.com/user-attachments/assets/67d969e4-5236-45ba-9349-d25404a24dfc)

## Assinaturas
![image](https://github.com/user-attachments/assets/3b94ce64-cd8d-49a9-a72c-50bcdc5ee95f)

## Grupos de gerenciamento
![image](https://github.com/user-attachments/assets/93c68164-e5ae-49bd-b464-4d4ea6690bf0)

Os grupos de gerenciamento podem incluir várias assinaturas do Azure. As assinaturas herdam as condições aplicadas ao grupo de gerenciamento.
