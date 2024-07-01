package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Service;

@Service
public class SampleService {

    private final SampleRepository sampleRepository;

    @Autowired
    public SampleService(final SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public SampleRepository getSampleRepository() {
        return sampleRepository;
    }
}
