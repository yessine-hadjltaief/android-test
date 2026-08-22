stage('Check Android Environment') {
    steps {
        sh '''
            echo "User:"
            whoami

            echo "ANDROID_HOME:"
            echo $ANDROID_HOME

            echo "ADB:"
            which adb

            adb version

            echo "SDK:"
            ls -la $ANDROID_HOME
        '''
    }
}
