/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.auth;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.client.KubernetesWatcher;


/**
 * @author wuheng09@gmail.com
 *
 */
public class KubernetesRBACTest {

	
	public static void main(String[] args) throws Exception {
		
		KubernetesClient all = new KubernetesClient("https://39.100.71.73:6443", 
				"eyJhbGciOiJSUzI1NiIsImtpZCI6IjJqbW9qdGxjdzkyUmpDUWxGbkJXdVJfUFY0c0NuYzVpOG9SN1Rla3E2eXcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWNsaWVudC10b2tlbi00cmx0NyIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWNsaWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjQ3NTBiYzUyLWY5ZTctNDQ5OS1iNTMwLTMyNjNlZDJiZmE5MyIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWNsaWVudCJ9.dgwfUziRsfdqxrPE0Ba_9N5fR3PHd8tBDO4q7vBbRUSMhlkY76fICI8eIDeoe3LnWdH3Eo5RxfOYfMSJA0qcUvEO2zM0q1-GIl4AV0svpvVp5-BbunDOjWYmOtBFnkSIQcXSwmhhBpBaGOeVtJCUS4dCkgfu_oJZRYc2glFGfVQ1b-dfR6gajMugcjmeH-a7bAzvpzHqFoNSV8ib1fiSONeeQGg13GYnenEwtbYp2wGfKsIXcWnw0u5Q9sU2ogEZrfkD2PYpoZD7b0dyZW-wYwvotRNWnCAEmASTzi2YYO49FDv3qRGsU4SWfE_TkBb9oQxhckkkCBtyu7WihkWjbg");
		
		KubernetesClient limited = new KubernetesClient("https://39.100.71.73:6443", 
				"eyJhbGciOiJSUzI1NiIsImtpZCI6IjJqbW9qdGxjdzkyUmpDUWxGbkJXdVJfUFY0c0NuYzVpOG9SN1Rla3E2eXcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InBvZG1nci10b2tlbi1jZDZxYyIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJwb2RtZ3IiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiIxNzIwY2E2Yi1hYzg5LTRhN2MtODU4YS1jNjVlZTg2ODBmNWQiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpwb2RtZ3IifQ.bPMOSb5e3OWuUa-3r9V-dCRTyX82AvP2ZpH_g3CGbPrZfRiM1GCmysKSAzRkPpcOJEKk7-CCich3mDja5WJqt6SvQVI0GgGD5jkKJWG9WQe7eb6gLbYuop2-ps3hCkn-fnfNkIEXND7CEjGvopuf_tvr4inqDEKu6qgEFFq3aFkHY5Sct79JXs9nGjYOyHPuGII5-snFFZ8dLDu6IMvkUgG001lNk2t0Yq6F93loqpSumNY97fmBPO_Dl0xH7X4DGHkbqiob-Hk1hgLwo0TA-yD-Icc96JSjsMS8g76ORJs2_DmS0hpq94F5YG_rpN3WimpvhYIXrKNAI4pDBv9lSQ", 
				all.getAnalyzer());
		
		KubernetesWatcher watcher = new KubernetesWatcher(limited) {
			
			@Override
			public void doModified(JsonNode node) {
				System.out.println(node);
			}
			
			@Override
			public void doDeleted(JsonNode node) {
				System.out.println(node);
			}
			
			@Override
			public void doAdded(JsonNode node) {
				System.out.println(node);
			}

			@Override
			public void doClose() {
				System.out.println("close");
			}

		};
		limited.watchResources("Pod", KubernetesConstants.VALUE_ALL_NAMESPACES, watcher);
	
	}


}
