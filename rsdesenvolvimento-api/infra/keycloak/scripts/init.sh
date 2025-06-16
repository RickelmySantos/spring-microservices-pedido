#!/bin/bash

# set +e
# set -x
FILE_CHECK=/tmp/initialized

init()
{
    if test -f "${FILE_CHECK}"; then
        echo ">>>>>>>>>>>>>>>>  KEYCLOAK JÁ CONFIGURADO ANTERIORMENTE  <<<<<<<<<<<<<<<<<<<<"
    else
        REALM_MASTER="master"
        REALM_DEFAULT="REALM_LOCAL"
        ADMIN_CLIENT_SERVICE_ACCOUNT_USER="service-account-keycloak-admin"

        ROLE_DELIMITER=","
        INDEX_ROLE_NAME=1
        INDEX_ROLE_DESCRIPTION=2

        USER_DELIMITER=","
        INDEX_USER_LOGIN=1
        INDEX_USER_FIRST_NAME=2
        INDEX_USER_LAST_NAME=3
        INDEX_USER_EMAIL=4

        USER_ROLE_MAPPING_DELIMITER=","
        INDEX_USER_ROLE_MAPPING_USER=1
        INDEX_USER_ROLE_MAPPING_ROLE=2

        SERVICE_ACCOUNTS_ROLES_DELIMITER=","
        INDEX_SERVICE_ACCOUNTS_ROLES_CLIENT_ID=1
        INDEX_SERVICE_ACCOUNTS_ROLES_ROLE=2

        echo ">>>>>>>>>>>>>>>>  CONFIGURANDO O KEYCLOAK  <<<<<<<<<<<<<<<<<<<<"
        while :
        do
            if (curl -s -f -S http://0.0.0.0:8080/auth)
            then
                echo "Configurando credenciais no Keycloak Admin Console..."
                kcadm.sh config credentials --server http://0.0.0.0:8080/auth --realm master --user admin --password admin --config ./tmp/kcadm.config 2>/dev/null

                echo "Criando o Realm ${REALM_DEFAULT}..."
                realmId="$(kcadm.sh create realms -s realm=${REALM_DEFAULT} -s enabled=true --id --config ./tmp/kcadm.config 2>/dev/null)"
                echo "Realm criado com sucesso: ${realmId}"


                echo "Configurando Client Admin..."
                adminClientId="$(kcadm.sh create clients -r ${REALM_DEFAULT} -f ./import/clients/keycloak-admin.json --id --config ./tmp/kcadm.config 2>/dev/null)"
                echo "Client Admin criado com sucesso: ${adminClientId}"


                echo "Configurando Client da Aplicação..."
                appClientId="$(kcadm.sh create clients -r ${REALM_DEFAULT} -f ./import/clients/rsdesenvolvimento-ui.json --id --config ./tmp/kcadm.config 2>/dev/null)"
                echo "Client da Aplicação criado com sucesso: ${appClientId}"

                echo "Configurando Client da Aplicação..."
                appClientId="$(kcadm.sh create clients -r ${REALM_DEFAULT} -f ./import/clients/rsdesenvolvimento-api.json --id --config ./tmp/kcadm.config 2>/dev/null)"
                echo "Client da Aplicação criado com sucesso: ${appClientId}"



                echo "Criando as Roles da Aplicação"
                while IFS= read -r line
                do
                    roleName="$(echo ${line} | cut -d "${ROLE_DELIMITER}" -f ${INDEX_ROLE_NAME})"
                    roleDescription="$(echo ${line} | cut -d "${ROLE_DELIMITER}" -f ${INDEX_ROLE_DESCRIPTION})"

                    kcadm.sh create clients/${appClientId}/roles -r ${REALM_DEFAULT} -s name="${roleName}" -s description="${roleDescription}" --id --config ./tmp/kcadm.config
                    echo "Role de Aplicação ${roleName} - ${roleDescription} criada com sucesso"
                done < './import/data/roles.csv'


                echo "Criando os Usuários do Realm"
                while IFS= read -r line
                do
                    userLogin="$(echo ${line} | cut -d "${USER_DELIMITER}" -f ${INDEX_USER_LOGIN})"
                    userFirstName="$(echo ${line} | cut -d "${USER_DELIMITER}" -f ${INDEX_USER_FIRST_NAME})"
                    userLastName="$(echo ${line} | cut -d "${USER_DELIMITER}" -f ${INDEX_USER_LAST_NAME})"
                    userEmail="$(echo ${line} | cut -d "${USER_DELIMITER}" -f ${INDEX_USER_EMAIL})"

                    kcadm.sh create users -r ${REALM_DEFAULT} -s username="${userLogin}" -s firstName="${userFirstName}" -s lastName="${userLastName}" -s email="${userEmail}" -s enabled=true --config ./tmp/kcadm.config
                    kcadm.sh set-password -r ${REALM_DEFAULT} --username "${userLogin}" --new-password "12345" --config ./tmp/kcadm.config

                    echo "Usuário ${userLogin} - ${userFirstName} ${userLastName} - ${userEmail} criado com sucesso"
                done < './import/data/users.csv'

                echo "Criando o mapeando das Roles com os Usuários"
                while IFS= read -r line
                do
                    userRoleMappingUser="$(echo ${line} | cut -d "${USER_ROLE_MAPPING_DELIMITER}" -f ${INDEX_USER_ROLE_MAPPING_USER})"
                    userRoleMappingRole="$(echo ${line} | cut -d "${USER_ROLE_MAPPING_DELIMITER}" -f ${INDEX_USER_ROLE_MAPPING_ROLE})"

                    kcadm.sh add-roles -r ${REALM_DEFAULT} --cid "${appClientId}" --rolename "${userRoleMappingRole}" --uusername "${userRoleMappingUser}"  --config ./tmp/kcadm.config
                    echo "Mapeamento da Role de Aplicação ${userRoleMappingRole} para o usuário ${userRoleMappingUser} criado com sucesso"
                done < './import/data/user-role-mapping.csv'


                echo "Criando o mapeando das Service Accounts Roles para o client Admin"
                while IFS= read -r line
                do
                    serviceAccountsRolesClientId="$(echo ${line} | cut -d "${SERVICE_ACCOUNTS_ROLES_DELIMITER}" -f ${INDEX_SERVICE_ACCOUNTS_ROLES_CLIENT_ID})"
                    serviceAccountsRolesRole="$(echo ${line} | cut -d "${SERVICE_ACCOUNTS_ROLES_DELIMITER}" -f ${INDEX_SERVICE_ACCOUNTS_ROLES_ROLE})"

                    kcadm.sh add-roles -r ${REALM_DEFAULT} --cclientid "${serviceAccountsRolesClientId}" --rolename "${serviceAccountsRolesRole}" --uusername "${ADMIN_CLIENT_SERVICE_ACCOUNT_USER}"  --config ./tmp/kcadm.config
                    echo "Mapeamento da Service Account Role ${serviceAccountsRolesRole} do Client ${serviceAccountsRolesClientId} para o usuário ${ADMIN_CLIENT_SERVICE_ACCOUNT_USER} criado com sucesso"
                done < './import/data/service-accounts-roles.csv'


                break;
            fi
            sleep 5
        done

        touch ${FILE_CHECK}
        echo ">>>>>>>>>>>>>>>>  CONFIGURAÇÃO DO KEYCLOAK REALIZADA COM SUCESSO  <<<<<<<<<<<<<<<<<<<<"
    fi
}

echo “Iniciando processo em background para configurar o Keycloak”
init & disown
echo “Processo em background para configurar o Keycloak Iniciado”
