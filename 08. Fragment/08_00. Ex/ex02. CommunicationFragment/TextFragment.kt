package com.frost.ex02communicationfragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.frost.ex02communicationfragment.databinding.FragmentTextBinding

class TextFragment : Fragment() {
    var binding: FragmentTextBinding?=null
    val data = arrayListOf<String>("ImageData1", "ImageData2", "ImageData3")
    val model:MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTextBinding.inflate(layoutInflater, container, false)
        return binding!!.root//null 값 아닌 것을 보장해야함
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //view 생성시 초기화 하기
        super.onViewCreated(view, savedInstanceState)
//        val i = requireActivity().intent
//        //requireActivity()는 null값이면 자동 예외처리
//        val imgNum = i.getIntExtra("imgNum", -1)
//       if(imgNum!=-1){//세로 모드이면 fragment가 SecondActivity에 부착되어 있음을 시사
//            binding!!.textView.text = data[imgNum]
//        }else{//가로 모드이면 fragment가 MainActivity에 부착되어 있음을 시사
            model.selectedNum.observe(viewLifecycleOwner, Observer{
                binding!!.textView.text = data[it]
            })
            //observe() 메서드는 selectedNum이라는 데이터가 변화되었는지 감지
            //변경되었으면 Observer라는 콜백함수 호출하도록 유도
            //it는 observe하고 있는 값
//        }
    }

    override fun onDestroyView() {
        //destroy가 되면 binding을 null로 만들어서 가비지 컬렉터가 메모리 누수 방지하도록 설정
        super.onDestroyView()
        binding=null
    }
}