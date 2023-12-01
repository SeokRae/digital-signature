# 비대칭 키를 이용한 암호화 통신 방식(Public Key Infrastructure, PKI)

> 배경

두 결제 처리 회사, '회사 A'와 '회사 B'가 있습니다. 
'회사 A'는 국제적인 온라인 결제 서비스를 제공하는 글로벌 기업이며, '회사 B'는 지역 시장에 특화된 결제 처리 서비스를 운영하는 기업입니다.

> 상황

'회사 A'는 자사의 결제 네트워크를 확장하고 '회사 B'와의 파트너십을 통해 지역 시장에 진입하기로 결정합니다. 
이들은 국제적인 결제 처리를 위해 정보 보안을 강화할 필요성을 느낍니다.

> 문제

서로 다른 지역과 시스템을 가진 두 회사 간의 데이터 전송은 다양한 보안 위험을 내포합니다. 
거래 정보의 **무결성**과 **비밀성**을 유지하는 것은 두 회사의 협력과 신뢰 구축에 핵심적입니다.

> 해결책

'회사 B'는 모든 거래 데이터에 Ed25519 디지털 서명을 사용하는 PKI 시스템을 도입할 것을 제안합니다. 
이 시스템은 강력한 보안과 데이터 무결성을 보장하며, 통신 과정에서의 안정성을 제공합니다.

'회사 A'와 '회사 B'는 Ed25519 기반의 디지털 서명 시스템을 도입합니다. 
이를 통해 모든 거래 데이터에 디지털 서명을 추가하여, 거래의 출처와 무결성을 확실히 할 수 있습니다.

## PKI (Public Key Infrastructure) 개요

1. 정의

   - PKI는 디지털 인증서와 공개 키 암호화를 사용하여, 네트워크 상에서 사용자와 시스템의 신원을 검증하고 안전한 통신을 가능하게 하는 기술입니다.

2. 구성 요소
   - **디지털 인증서**: 사용자의 공개 키와 신원 정보를 포함하고, 신뢰할 수 있는 기관(CA, Certificate Authority)에 의해 서명됩니다.
   - **공개 키와 개인 키**: 사용자는 공개 키를 공개적으로 배포하고, 개인 키는 비밀리에 보관합니다. 이 두 키는 수학적으로 연결되어 있습니다.
   - **인증 기관(CA)**: 디지털 인증서를 발급하고 관리하는 신뢰할 수 있는 기관입니다.
   - **등록 기관(RA)**: CA의 보조 역할을 하여 신원 검증 및 인증서 발급을 돕습니다.

3. 용도

   - PKI는 웹사이트의 SSL/TLS 인증, 이메일 암호화 및 디지털 서명, VPN 접속 등 다양한 보안 목적으로 사용됩니다.


## Ed25519 디지털 서명 알고리즘

1. 개요

   - Ed25519는 공개 키 암호화에 사용되는 디지털 서명 알고리즘입니다. 
   - 이는 속도가 빠르고, 보안 수준이 높으며, 사이드 채널 공격에 강한 것으로 알려져 있습니다.

2. 특징

   - 보안성: Ed25519는 충돌 방지와 타이밍 공격에 강합니다.
   - 효율성: 낮은 연산 복잡도로 빠른 서명 생성 및 검증이 가능합니다.
   - 유연성: 다양한 플랫폼 및 어플리케이션에서 쉽게 구현될 수 있습니다.

3. 적용: Ed25519는 주로 서버와 클라이언트 간의 안전한 통신, 소프트웨어 패키지의 서명, VPN 인증 등에 사용됩니다.


## 테스트 프로젝트 개요

1. 목적
    - Ed25519를 사용하여 PKI 시스템의 보안과 효율성을 실험하고, 디지털 서명의 생성 및 검증 과정을 이해합니다.

2. 절차

   - 개발 환경 설정: 필요한 라이브러리와 도구를 설치합니다.
   - **키 생성**: Ed25519 알고리즘을 이용하여 공개 키와 개인 키를 생성합니다.
   - **서명 생성** 및 **검증**: 개인 키로 데이터를 서명하고, 공개 키를 사용하여 이 서명을 검증합니다.
   - **통합 테스트**: 실제 네트워크 환경에서 데이터 전송 및 서명 검증 절차를 시험합니다.

3. 기대 결과:

   - **이해 증진**: PKI와 Ed25519의 작동 원리에 대한 이해를 높일 수 있습니다.
   - **보안성 평가**: 실제 환경에서의 Ed25519의 보안성과 효율성을 평가합니다.
