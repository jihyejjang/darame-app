# 다람이-image segmentation을 이용한 사진편집어플

### 교내 상생스터디(2020.04~2020.06) + 이후로 더 발전시키는 중

-----------------

__주제__: DeepLearning-segmentation을 이용한 사진 편집 어플

__제안배경__: 기존의 사진 편집 어플은 사용자가 수동으로 배경을 지우거나 블러처리(모자이크)를 해야하는 번거로움이 있음<br>
-> 간편한 배경합성과 모자이크를 수행할 수 있는 **DeepLearning-segmentation** 기술을 적용하게 됨

------------

- __기능__: 
  - 배경합성: **영상1**의 원하는 **마스크**(**전경**부분) + **영상2**의 **배경**
  - 모자이크: 영상에서 **원하는 마스크**를 **제외**한 **나머지** **object**는 **모자이크**처리

- __주요 활용기술__:
  - instance segmentation:
  - mosaic: 
  - socket: 
  
- __흐름도__:
  - 초기화면
  <img src="https://user-images.githubusercontent.com/47767202/85769029-95685500-b754-11ea-947f-bd77326e4b8f.png" width="30%">

   - 카메라 선택
   <img src="https://user-images.githubusercontent.com/47767202/85877683-81832880-b812-11ea-8e65-5108530acd01.png" width="30%">
   
   - 합성 선택
   <img src="https://user-images.githubusercontent.com/47767202/85877737-965fbc00-b812-11ea-9228-7dae6b4d438d.png" width="30%">

//- ~4월: [segmentation 관련 스터디](https://github.com/sohyeon98720/deepLearning_study#%EC%BD%94%EB%93%9C%EC%9A%94%EC%95%BD-segmentation)
